package BackEnd.Algorithms;

import BackEnd.Project.Project;

import java.util.ArrayList;
import java.util.List;


public class Composition
{
	private boolean ReqSatisfied;
	private float value=0;
	
	private List<Project> composition;
	
	public Composition()
	{
		ReqSatisfied=true;
		composition=new ArrayList<Project>();
	}

	public void AddProject(Project project)
	{
		composition.add(project);
		value+=project.getTotalValue();
	}

	public List<Project> getProjects()
	{
		return composition;
	}

	public Project GetProject(int index)
	{
		return composition.get(index);
	}
	
	public int ProjectCount()
	{
		return composition.size();
	}
	
	public boolean AreReqSatisfied()
	{
		return ReqSatisfied;
	}

	public void SetReqSatisfied(boolean reqSatisfied)
	{
		ReqSatisfied = reqSatisfied;
	}

	public float GetValue()
	{
		return value;
	}
	
	public void display()
	{
		for(Project project:composition)
		{
			System.out.print(project.getLabel()+" ");
		}
		System.out.println("with a value of : "+value);
	}

	public void display2()
	{
		for(Project project:composition)
		{
			System.out.print(project.getLabel()+" ");
		}
	}

	public String GetCompositionString(List<Project> Allprojects)
	{
		String CompositionString = "";
		int index=0;
		String binary="("; 
		for(Project allProject:Allprojects)
		{
			int exist=0;
			for (Project project : composition)
			{
				if(index==0)CompositionString = CompositionString + project.getLabel() + " ";
				if(project.getId()==allProject.getId())
				{
					exist=1;
				}
			}
			if(index==Allprojects.size()-1)
			{
				binary+=""+exist;
			}
			else
			{
				binary+=exist+",";
			}
			index++;
		}
		binary+=")";
		
		return CompositionString+binary;
	}
}
