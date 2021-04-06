package BackEnd.Resource;

public class Resource
{
	private int id;
	private int idCategory;
	private String label;

	public Resource(int id,String label,int idCategory)
	{
		this.id=id;
		this.label=label;
		this.idCategory=idCategory;
	}

	public int getId()
	{
		return id;
	}
	
	public String getLabel()
	{
		return label;
	}

	public int getIdCategory()
	{
		return idCategory;
	}
	
}
