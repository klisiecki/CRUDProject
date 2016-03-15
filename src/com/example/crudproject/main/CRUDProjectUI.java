package com.example.crudproject.main;

import javax.servlet.annotation.WebServlet;

import com.example.crudproject.model.CRUDModel;
import com.example.crudproject.presenter.CRUDPresenter;
import com.example.crudproject.view.CRUDViewImpl;
import com.example.crudproject.view.Messages;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("crudproject")
public class CRUDProjectUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = CRUDProjectUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		CRUDModel model = new CRUDModel();
		CRUDViewImpl view = new CRUDViewImpl();
		
		try {
			model.initDB();
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
		
		new CRUDPresenter(model, view);
		Page.getCurrent().setTitle(Messages.getString("PAGE_TITLE"));
		setContent(view);
	}
}