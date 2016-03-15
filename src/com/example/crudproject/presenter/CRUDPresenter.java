package com.example.crudproject.presenter;

import java.sql.SQLException;

import com.example.crudproject.model.CRUDModel;
import com.example.crudproject.model.MyItem;
import com.example.crudproject.view.CRUDView;

public class CRUDPresenter implements CRUDView.ViewListener {
	private CRUDModel model;
	private CRUDView view;

	public CRUDPresenter(CRUDModel model, CRUDView view) {
		this.model = model;
		this.view = view;
		this.view.addListener(this);
		try {
			view.setItems(model.getAllItems());
		} catch (SQLException e) {
			view.showError(e.getMessage());
		}
	}

	@Override
	public void addItemButtonClick(MyItem item) {
		try {
			model.addItem(item);
			view.setItems(model.getAllItems());

		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	@Override
	public void deleteItemButtonClick(int id) {
		try {
			model.deleteItem(id);
			view.setItems(model.getAllItems());
		} catch (Exception e) {
			view.showError(e.getMessage());
		}

	}

	@Override
	public void addRandomItemButtonClick() {
		try {
			model.addRandomItem();
			view.setItems(model.getAllItems());
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	@Override
	public void editItemButtonClick(MyItem item) {
		try {
			model.editItem(item);
			view.setItems(model.getAllItems());
		} catch(Exception e) {
			view.showError(e.getMessage());
		}
	}
}
