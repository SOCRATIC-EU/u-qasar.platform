package eu.uqasar.web.dashboard.widget.tech_debt;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ro.fortsoft.wicket.dashboard.Dashboard;
import ro.fortsoft.wicket.dashboard.Widget;
import ro.fortsoft.wicket.dashboard.web.DashboardContext;
import ro.fortsoft.wicket.dashboard.web.DashboardContextAware;
import ro.fortsoft.wicket.dashboard.web.DashboardPanel;
import ro.fortsoft.wicket.dashboard.web.WidgetPanel;
import eu.uqasar.model.dashboard.DbDashboard;
import eu.uqasar.model.tree.Project;
import eu.uqasar.service.tree.TreeNodeService;
import eu.uqasar.web.dashboard.DashboardViewPage;

public class TechDebtChartSettingsPanel extends GenericPanel<TechDebtChartWidget> 
	implements DashboardContextAware {

	private static final long serialVersionUID = 1L;
	
	private transient DashboardContext dashboardContext;
	private String projectName;
	private Project project;
	private String chartType;
	private List<Project> projects;
	
	public TechDebtChartSettingsPanel(String id, IModel<TechDebtChartWidget> model) {
		super(id, model);
		
		setOutputMarkupPlaceholderTag(true);
		
		Form<Widget> form = new Form<Widget>("form");

		// Get the project from the settings
		projectName = getModelObject().getSettings().get("project");

		// DropDown select for Projects
		TreeNodeService treeNodeService = null;
		
		try {
			InitialContext ic = new InitialContext();
			treeNodeService = (TreeNodeService) ic.lookup("java:module/TreeNodeService");   
			projects = treeNodeService.getAllProjectsOfLoggedInUser();
			if (projects != null && projects.size() != 0) {
				if (projectName == null || projectName.isEmpty()) {
					projectName = projects.get(0).getName();
				}   
				project = treeNodeService.getProjectByName(projectName);
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
        DropDownChoice<Project> projectChoice = new DropDownChoice<Project>("project", new PropertyModel<Project>(this, "project"), projects);
        form.add(projectChoice);
		
		// Field for the chart type
		chartType = getModelObject().getSettings().get("chartType");
        DropDownChoice<String> choice = new DropDownChoice<String>("chartType", 
        		new PropertyModel<String>(this, "chartType"), TechDebtChartWidget.TYPES);
        form.add(choice);
        
        form.add(new AjaxSubmitLink("submit") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (project != null) {
					getModelObject().getSettings().put("chartType", chartType);
					getModelObject().getSettings().put("project", project.getName());
					Dashboard dashboard = findParent(DashboardPanel.class).getDashboard();
					DbDashboard dbdb = (DbDashboard) dashboard;

					WidgetPanel widgetPanel = findParent(WidgetPanel.class);
					TechDebtChartWidgetView widgetView = (TechDebtChartWidgetView) widgetPanel.getWidgetView();
					target.add(widgetView);
					hideSettingPanel(target);

            		// Do not save the default dashboard
					if (dbdb.getId() != null && dbdb.getId() != 0) {
						dashboardContext.getDashboardPersiter().save(dashboard);
						PageParameters params = new PageParameters();
						params.add("id", dbdb.getId());
						setResponsePage(DashboardViewPage.class, params);
					}
				}
            }

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

        });
        form.add(new AjaxLink<Void>("cancel") {

            private static final long serialVersionUID = 1L;

			@Override
            public void onClick(AjaxRequestTarget target) {
            	hideSettingPanel(target);
            }
            
        });
        
		add(form);
	}
	

	@Override
	public void setDashboardContext(DashboardContext dashboardContext) {
		this.dashboardContext = dashboardContext;
	}
	
	private void hideSettingPanel(AjaxRequestTarget target) {
    	setVisible(false);
    	target.add(this);
	}

	/**
	 * 
	 * @return
	 */
	public String getChartType() {
		return chartType;
	}

	/**
	 * 
	 * @param chartType the chartType to set
	 */
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}	

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}


	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}


	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}
}