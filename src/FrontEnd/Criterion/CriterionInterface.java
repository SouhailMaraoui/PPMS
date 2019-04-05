package FrontEnd.Criterion;

import BackEnd.Criterion.CriterionQueries;
import BackEnd.CriterionType.CriterionTypeQueries;
import FrontEnd.Home;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.List;

public class CriterionInterface extends Pane
{
    static double scalex = Home.scalex;
    static double scaley = Home.scaley;

    private Paint black=Paint.valueOf("000000");
    private Paint red=Paint.valueOf("F04040");
    private Paint lightBlue=Paint.valueOf("5096be");
    private Paint lightGreen=Paint.valueOf("50be96");

    private Button add,bar,modify;
    private Button add2,bar2,modify2;
    private TableView tvCriteria, tvType;

    @SuppressWarnings({ "unchecked","rawtypes" })
    public CriterionInterface(double x,double y)
    {
        this.setLayoutX(x*scalex);
        this.setLayoutY(y*scaley);

        Image modifyIcon= new Image("file:res/icon/resource/Modify.png");

        bar=JavaFX.NewButton("",black,2, 780, 95,550,10);
        modify=JavaFX.NewButton("Modifier le critère", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,95 , 220, 32);

        getChildren().add(bar);
        getChildren().add(modify);

        setActionBar(false,0);

        tvCriteria = JavaFX.NewTableView(CriterionQueries.getResultSet(), 100,75, 1000, 400);
        Pane additionalOptions=new Pane();
        additionalOptions.setLayoutX(1125*scalex);
        additionalOptions.setLayoutY(75*scaley);
        modify.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActionBar(false,0);
            setActionBar2(false,0);
            setActive(false);
            CriterionModify criterionModify=new CriterionModify(this,0);
            additionalOptions.getChildren().add(criterionModify);
        });
        getChildren().add(additionalOptions);
        tvCriteria.setRowFactory(tv->
        {
            TableRow row = new TableRow<>();
            row.setOnMousePressed(event->
            {
                if (!row.isEmpty())
                {
                    setActionBar2(false,0);
                    setActionBar(true,(int) (95+row.getIndex()*25/scaley));
                    additionalOptions.getChildren().clear();
                }
            });
            return row;
        });

        add=JavaFX.NewButton("Ajouter",18,7,482);
        getChildren().add(add);

        Pane addPane=new Pane();
        List<String> Types=CriterionTypeQueries.getCriteriaTypeRef();
        String[] genres={"positif","négatif"};
        TextField idField=JavaFX.NewTextField(18,200,100,482);

        idField.setEditable(false);
        TextField refField=JavaFX.NewTextField(18,200,300,482);
        TextField libField=JavaFX.NewTextField(18,200,500,482);
        ComboBox genreField=JavaFX.NewComboBox(genres,200,700,482);
        ComboBox typeField=JavaFX.NewComboBox(Types,200,900,482);

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,1110,482);
        Button cancel=JavaFX.NewButton("Annuler",red,18,1230,482);
        Button addBar=JavaFX.NewButton("",black,2, 93, 493,1200,10);

        add.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActive(false);
            idField.setText(String.valueOf(tvCriteria.getItems().size()));
            addPane.getChildren().addAll(addBar,idField,refField,libField,genreField,typeField,confirm,cancel);
            setActionBar(false,0);
            setActionBar2(false,0);
            tvCriteria.getSelectionModel().clearSelection();
            tvType.getSelectionModel().clearSelection();
        });

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            int id=Integer.valueOf(idField.getText());
            String ref=refField.getText();
            String label=libField.getText();
            String genre= (String) genreField.getSelectionModel().getSelectedItem();
            int idType=typeField.getSelectionModel().getSelectedIndex();
            CriterionQueries.addToDatabase(id,ref,label,genre,idType);
            refreshTable();
            idField.setText("");libField.setText("");refField.setText("");
            addPane.getChildren().clear();
            setActive(true);
        });

        cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            addPane.getChildren().clear();
            setActive(true);
        });
        getChildren().add(addPane);

        //-----------------------------------------------------------------------------------------------------------------------------------------------
        bar2=JavaFX.NewButton("",black,2, 780, 95,550,10);
        modify2=JavaFX.NewButton("Modifier le type", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,95 , 240, 32);

        getChildren().add(bar2);
        getChildren().add(modify2);

        setActionBar2(false,0);

        tvType =JavaFX.NewTableView(CriterionTypeQueries.getResultSet(), 100,525, 1000, 420);
        Pane additionalOptions2=new Pane();
        additionalOptions2.setLayoutX(1125*scalex);
        additionalOptions2.setLayoutY(525*scaley);
        modify2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActionBar(false,0);
            setActionBar2(false,0);
            setActive(false);
            TypeModify typeModify=new TypeModify(this,0);
            additionalOptions2.getChildren().add(typeModify);
        });
        getChildren().add(additionalOptions2);
        tvType.setRowFactory(tv->
        {
            TableRow row = new TableRow<>();
            row.setOnMousePressed(event->
            {
                if (!row.isEmpty())
                {
                    setActionBar2(true,(int) (545+row.getIndex()*25/scaley));
                    setActionBar(false,0);
                    additionalOptions2.getChildren().clear();
                }
            });
            return row;
        });

        getChildren().add(tvCriteria);
        getChildren().add(tvType);

        //-----------------------------------------------------------------------------------------------------------------------------------------------
        add2=JavaFX.NewButton("Ajouter",18,7,950);
        getChildren().add(add2);

        Pane addPane2=new Pane();

        TextField id2Field=JavaFX.NewTextField(18,333,100,950);
        id2Field.setText(String.valueOf(tvType.getItems().size()));
        id2Field.setEditable(false);
        TextField ref2Field=JavaFX.NewTextField(18,333,433,950);
        TextField lib2Field=JavaFX.NewTextField(18,333,766,950);
        Button confirm2=JavaFX.NewButton("Confirmer",lightGreen,18,1110,950);
        Button cancel2=JavaFX.NewButton("Annuler",red,18,1230,950);
        Button addBar2=JavaFX.NewButton("",black,2, 93, 961,1200,10);

        add2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActive(false);
            addPane2.getChildren().addAll(addBar2,id2Field,ref2Field,lib2Field,confirm2,cancel2);
            setActionBar(false,0);
            setActionBar2(false,0);
            tvCriteria.getSelectionModel().clearSelection();
            tvType.getSelectionModel().clearSelection();
        });

        confirm2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            int id=Integer.valueOf(id2Field.getText());
            String ref=ref2Field.getText();
            String label=lib2Field.getText();

            CriterionTypeQueries.addToDatabase(id,ref,label);
            refreshTable2();
            typeField.getItems().add(ref);
            typeField.getSelectionModel().select(0);

            addPane2.getChildren().clear();
            setActive(true);
        });

        cancel2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            addPane2.getChildren().clear();
            setActive(true);
        });
        getChildren().add(addPane2);
    }

    private void setActionBar(boolean visbility,int position)
    {
        modify.setLayoutY(position*scaley);
        bar.setLayoutY((position+11)*scaley);

        modify.setVisible(visbility);
        bar.setVisible(visbility);
    }


    private void setActionBar2(boolean visbility,int position)
    {
        modify2.setLayoutY(position*scaley);
        bar2.setLayoutY((position+11)*scaley);

        modify2.setVisible(visbility);
        bar2.setVisible(visbility);
    }

    public void setActive(boolean bool)
    {
        add.setDisable(!bool);
        add2.setDisable(!bool);
        tvType.setDisable(!bool);
        tvCriteria.setDisable(!bool);
    }

    public void resetSelection()
    {
        setActive(true);
        tvCriteria.getSelectionModel().clearSelection();
        tvType.getSelectionModel().clearSelection();
    }

    public void refreshTable()
    {
        JavaFX.updateTable(tvCriteria,CriterionQueries.getResultSet());
    }
    public void refreshTable2()
    {
        JavaFX.updateTable(tvType,CriterionTypeQueries.getResultSet());
    }
}
