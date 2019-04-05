package FrontEnd.Project;

import BackEnd.Portfolio.PortfolioQueries;
import BackEnd.Project.Project;
import BackEnd.Project.ProjectQueries;
import BackEnd.ProjectType.ProjectTypeQueries;
import BackEnd.ResToProject.ResToProjectQueries;
import BackEnd.Resource.AssignedResource;
import FrontEnd.Home;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

import static BackEnd.Utility.getDatetime;


public class ProjectModify extends Pane
{
    static double scalex = Home.scalex;
    static double scaley = Home.scaley;

    private Paint black= Paint.valueOf("000000");
    private Paint grey= Paint.valueOf("E9E9E9");
    private Paint white= Paint.valueOf("FFFFFF");
    private Paint red= Paint.valueOf("F04040");
    private Paint lightOrange= Paint.valueOf("F77D50");
    private Paint lightBlue= Paint.valueOf("5096be");
    private Paint lightGreen= Paint.valueOf("50be96");

    public ArrayList<ResourcePane> resourcePaneList;

    public ProjectModify(ProjectInterface parent, int idProject)
    {
        Project project= ProjectQueries.getProjectById(idProject);
        ResourcePane.count=0;
        this.setPrefWidth(575*scalex);
        this.setPrefHeight(400*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");

        Label libLB=JavaFX.NewLabel("Libellé",lightBlue,1,20,10,10);
        TextField libField=JavaFX.NewTextField(18,200,10,35);
        libField.setText(project.getLabel());

        Label porLB=JavaFX.NewLabel("Portefeuille",lightBlue,1,20,250,10);
        ComboBox porBox=JavaFX.NewComboBox(PortfolioQueries.getPortfoliosRef(),250,250,35);
        porBox.getSelectionModel().select(project.getIdPortfolio());

        Label typeLB=JavaFX.NewLabel("Type",lightBlue,1,20,10,75);
        ComboBox typeBox=JavaFX.NewComboBox(ProjectTypeQueries.getProjectTypesRef(),200,10,100);
        typeBox.getSelectionModel().select(project.getIdType());


        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,350);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,350);

        getChildren().addAll(libLB,libField,porLB,porBox,typeLB,typeBox,confirm,cancel);

        //-Resource-----------------------------------------------------------------------------------------------------------------------------
        int y=110;
        getChildren().add(JavaFX.NewLabel("Ressources",lightBlue,1,20,10,y+=50));
        List<AssignedResource> resourcesList= ResToProjectQueries.getResourceByProject(idProject);
        Pane resourcePanes=new Pane();
        resourcePanes.setLayoutY((y+50)*scaley);
        resourcePaneList= new ArrayList<>();

        int resoureIndex=0;
        for(AssignedResource resource:resourcesList)
        {
            ResourcePane rp=new ResourcePane(resourcePanes,resourcePaneList,resource);
            resourcePaneList.add(rp);
            resourcePanes.getChildren().add(rp);
        }

        getChildren().add(resourcePanes);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            String label=libField.getText();
            int idPor=porBox.getSelectionModel().getSelectedIndex();
            int idType=typeBox.getSelectionModel().getSelectedIndex();
            if(!label.equals(project.getLabel()) || idType!=project.getIdType() || idPor!=project.getIdPortfolio())
            {
                /*if(idPor!=project.getIdPortfolio())
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Look, a Warning Dialog");
                    alert.setContentText("Careful with the next step!");
                    alert.showAndWait();
                }*/
                ProjectQueries.updateProject(idProject,label,idPor,idType);
            }

            ResToProjectQueries.resetTable(idProject);
            for(ResourcePane rp:resourcePaneList)
            {

                ResToProjectQueries.addToDatabase(idProject,rp.getIdResource(),getDatetime(),rp.getQuantity());
            }
            parent.refreshTable();
            this.setStyle("-fx-background-color: #f3f3f3;");
            parent.resetSelection();
            getChildren().clear();
        });

        cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            parent.resetSelection();
            getChildren().clear();
            this.setStyle("-fx-background-color: #f3f3f3;");
        });
    }

}

class ResourcePane extends Pane
{
    static double scaley = Home.scaley;

    public static int count=0;
    public int index;

    private int idResource;
    private int quantity;
    private TextField resourcesCountField;

    private Paint lightOrange= Paint.valueOf("F77D50");

    ResourcePane(Pane resourcePane, ArrayList<ResourcePane> resourceList,AssignedResource resource)
    {
        index=count;
        count++;

        this.idResource=resource.getIdResource();
        String resourceName=resource.getLabel();
        this.quantity=resource.getQuantity();

        setLayoutY((index*50)*scaley);
        getChildren().add(JavaFX.NewLabel(resourceName,18,50,0));
        resourcesCountField=JavaFX.NewTextField(18,75,390,0);
        resourcesCountField.setText(String.valueOf(quantity));
        Button delete=JavaFX.NewButton("Supprimer",lightOrange,15,475,0);

        delete.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            resourcePane.getChildren().clear();
            resourceList.remove(this);
            int i=0;
            for(ResourcePane rp:resourceList)
            {
                rp.index=i;
                rp.setLayoutY(i*50);
                resourcePane.getChildren().add(rp);
                i++;
            }
            count--;
        });
        getChildren().addAll(resourcesCountField,delete);
    }

    public int getIdResource()
    {
        return idResource;
    }

    public int getQuantity()
    {
        return Integer.valueOf(resourcesCountField.getText());
    }
}
