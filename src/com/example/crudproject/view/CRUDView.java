package com.example.crudproject.view;

import java.util.List;

import com.example.crudproject.model.MyItem;

public interface CRUDView {

	interface ViewListener {
		void addItemButtonClick(MyItem item);

		void addRandomItemButtonClick();

		void editItemButtonClick(MyItem item);

		void deleteItemButtonClick(int id);
	}

	public void showError(String message);

	public void setItems(List<MyItem> items);

	public void addListener(ViewListener listener);
}
