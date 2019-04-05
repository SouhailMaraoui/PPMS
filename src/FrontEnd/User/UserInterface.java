package FrontEnd.User;

import BackEnd.Profile.ProfileQueries;
import BackEnd.User.UserQueries;
import FrontEnd.Home;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.List;

public class UserInterface extends Pane
{
	static double scalex = Home.scalex;
	static double scaley = Home.scaley;

	private Paint black=Paint.valueOf("000000");
	private Paint red=Paint.valueOf("F04040");
	private Paint lightBlue=Paint.valueOf("5096be");
	private Paint lightGreen=Paint.valueOf("50be96");

	private Button add,bar,modify,disable;
	private Button add2,bar2,modify2;

	private TableView tvUser,tvProfile;

	@SuppressWarnings({ "unchecked","rawtypes" })
	public UserInterface(double x,double y)
	{
		this.setLayoutX(x*scalex);
		this.setLayoutY(y*scaley);
		
		Image modifyIcon= new Image("file:res/icon/user/Modify.png");
		Image disableIcon= new Image("file:res/icon/user/Disable.png");

		//--User-Table--------------------------------------------------------------------------------------------------------------------------------
		bar=JavaFX.NewButton("",black,2, 780, 95,775,10);
		modify=JavaFX.NewButton("Modifier l'utilisateur", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,95 , 220, 32);
		disable=JavaFX.NewButton("Desactiver l'utilisateur", disableIcon, ContentDisplay.LEFT, red, 16,1340 ,95 , 235, 32);
		
		getChildren().add(bar);
		getChildren().add(modify);
		getChildren().add(disable);

		setActionBar(false,0);
	
		tvUser=JavaFX.NewTableView(UserQueries.getResultSet(), 100,75, 1000, 400);
		Pane additionalOptions=new Pane();

		tvUser.setRowFactory(tv->
		{
			TableRow row = new TableRow<>();
			row.setOnMousePressed(event->
			{
				if (!row.isEmpty())
				{
					setActionBar2(false,0);
					setActionBar(true,(int) (95+row.getIndex()*25/scaley));
					additionalOptions.getChildren().clear();
					tvProfile.getSelectionModel().clearSelection();
				}
			});
			return row;
		});
		additionalOptions.setLayoutX(1125*scalex);
		additionalOptions.setLayoutY(75*scaley);

		modify.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false,0);
			setActionBar2(false,0);
			setActive(false);
			UserModify userModify=new UserModify(this,0);
			additionalOptions.getChildren().add(userModify);
		});
		getChildren().add(tvUser);
		getChildren().add(additionalOptions);

		//-Add-User-----------------------------------------------------------------------------------------------------------------------------------------
		add=JavaFX.NewButton("Ajouter",18,7,482);
		getChildren().add(add);

		Pane addPane=new Pane();
		List<String> Profiles=ProfileQueries.getProfilesRef();
		TextField idField=JavaFX.NewTextField(18,166,100,482);
		TextField usrField=JavaFX.NewTextField(18,167,266,482);
		TextField fsField=JavaFX.NewTextField(18,167,433,482);
		TextField lsField=JavaFX.NewTextField(18,167,600,482);
		TextField phField=JavaFX.NewTextField(18,167,767,482);
		ComboBox idProField=JavaFX.NewComboBox(Profiles,167,934,482);
		idField.setText(String.valueOf(tvUser.getItems().size()));
		idField.setEditable(false);
		Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,1110,482);
		Button cancel=JavaFX.NewButton("Annuler",red,18,1230,482);
		Button addBar=JavaFX.NewButton("",black,2, 93, 493,1200,10);

		add.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false,0);
			setActionBar2(false,0);
			setActive(false);
			addPane.getChildren().addAll(addBar,idField,usrField,fsField,lsField,phField,idProField,confirm,cancel);
			tvUser.getSelectionModel().clearSelection();
			tvProfile.getSelectionModel().clearSelection();
		});

		confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			int id=Integer.valueOf(idField.getText());
			String username=usrField.getText();
			String firstname=fsField.getText();
			String lastname=lsField.getText();
			String phone=phField.getText();
			int idProfile=idProField.getSelectionModel().getSelectedIndex();
			UserQueries.addToDatabase(id,username,firstname,lastname,phone,idProfile);
			refreshTable();
			addPane.getChildren().clear();
			setActive(true);
		});

		cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			addPane.getChildren().clear();
			setActive(true);
		});
		getChildren().add(addPane);

		//--Profile-Table--------------------------------------------------------------------------------------------------------------------------------
		bar2=JavaFX.NewButton("",black,2, 780, 95,500,10);
		modify2=JavaFX.NewButton("Modifier le profile", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,95 , 200, 32);

		getChildren().add(bar2);
		getChildren().add(modify2);

		setActionBar2(false,0);

		tvProfile=JavaFX.NewTableView(ProfileQueries.getResultSet(), 100,525, 1000, 420);

		Pane additionalOptions2=new Pane();
		additionalOptions2.setLayoutX(1125*scalex);
		additionalOptions2.setLayoutY(525*scaley);
		modify2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false,0);
			setActionBar2(false,0);
			setActive(false);
			ProfileModify profileModify=new ProfileModify(this,0);
			additionalOptions2.getChildren().add(profileModify);
		});
		getChildren().add(additionalOptions2);
		tvProfile.setRowFactory(tv->
		{
			TableRow row = new TableRow<>();
			row.setOnMousePressed(event->
			{
				if (!row.isEmpty())
				{
					setActionBar2(true,(int) (545+row.getIndex()*25/scaley));
					setActionBar(false,0);
					tvUser.getSelectionModel().clearSelection();
					additionalOptions2.getChildren().clear();
				}
			});
			return row;
		});
		
		getChildren().add(tvProfile);

		//-Add-Profile-----------------------------------------------------------------------------------------------------------------------------------------
		add2=JavaFX.NewButton("Ajouter",18,7,950);
		getChildren().add(add2);

		Pane addPane2=new Pane();

		TextField id2Field=JavaFX.NewTextField(18,333,100,950);
		id2Field.setText(String.valueOf(tvProfile.getItems().size()));
		id2Field.setEditable(false);
		TextField ref2Field=JavaFX.NewTextField(18,333,433,950);
		TextField lib2Field=JavaFX.NewTextField(18,333,766,950);
		Button confirm2=JavaFX.NewButton("Confirmer",lightGreen,18,1110,950);
		Button cancel2=JavaFX.NewButton("Annuler",red,18,1230,950);
		Button addBar2=JavaFX.NewButton("",black,2, 93, 961,1200,10);

		add2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false,0);
			setActionBar2(false,0);
			setActive(false);
			addPane2.getChildren().addAll(addBar2,id2Field,ref2Field,lib2Field,confirm2,cancel2);
			tvUser.getSelectionModel().clearSelection();
			tvProfile.getSelectionModel().clearSelection();
		});

		confirm2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			int id=Integer.valueOf(id2Field.getText());
			String ref=ref2Field.getText();
			String label=lib2Field.getText();
			ProfileQueries.addToDatabase(id,ref,label);
			refreshTable2();
			idProField.getItems().add(ref);
			idProField.getSelectionModel().select(0);
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
		disable.setLayoutY(position*scaley);
		bar.setLayoutY((position+11)*scaley);

		modify.setVisible(visbility);
		disable.setVisible(visbility);
		bar.setVisible(visbility);
	}

	private void setActionBar2(boolean visbility,int position)
	{
		modify2.setLayoutY(position*scaley);
		bar2.setLayoutY((position+11)*scaley);

		modify2.setVisible(visbility);
		bar2.setVisible(visbility);
	}
	public void resetSelection()
	{
		setActive(true);
		tvUser.getSelectionModel().clearSelection();
		tvProfile.getSelectionModel().clearSelection();
	}

	public void setActive(boolean bool)
	{
		add.setDisable(!bool);
		add2.setDisable(!bool);
		tvUser.setDisable(!bool);
		tvProfile.setDisable(!bool);
	}

	public void refreshTable()
	{
		JavaFX.updateTable( tvUser,UserQueries.getResultSet());
	}
	public void refreshTable2()
	{
		JavaFX.updateTable( tvProfile,ProfileQueries.getResultSet());
	}
}
