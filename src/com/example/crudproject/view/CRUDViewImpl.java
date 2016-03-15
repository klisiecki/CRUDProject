package com.example.crudproject.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.crudproject.model.MyItem;
import com.example.crudproject.view.MyItemEditorWindow.Mode;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class CRUDViewImpl extends CustomComponent implements CRUDView {
	private static final String ID_COLUMN = "id";
	private static final String NAME_COLUMN = "name";
	private static final String DATE_COLUMN = "date";

	private List<ViewListener> listeners = new ArrayList<ViewListener>();
	private Table itemList = new Table();
	private MyItemEditorWindow editWindow;

	private Button newItemButton = new Button(Messages.getString("NEW_ITEM")); 
	private Button editItemButton = new Button(Messages.getString("EDIT_ITEM")); 
	private Button addRandomItemButton = new Button(Messages.getString("ADD_RANDOM_ITEM"));
	private Button deleteItemButton = new Button(Messages.getString("DELETE_ITEM"));

	public CRUDViewImpl() {
		editWindow = new MyItemEditorWindow(this);
		initLayout();
		initItemList();
		initButtons();
	}

	public void initLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		layout.addComponent(itemList);
		layout.setSizeFull();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(newItemButton);
		buttonsLayout.addComponent(editItemButton);
		buttonsLayout.addComponent(deleteItemButton);
		layout.addComponent(buttonsLayout);
		layout.addComponent(addRandomItemButton);

		setCompositionRoot(layout);
	}

	public void initButtons() {
		newItemButton.setClickShortcut(KeyCode.N);
		editItemButton.setClickShortcut(KeyCode.E);
		deleteItemButton.setClickShortcut(KeyCode.DELETE);
		addRandomItemButton.setClickShortcut(KeyCode.R);
		
		editItemButton.setEnabled(false);
		deleteItemButton.setEnabled(false);
		
		newItemButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				openEditorWindow(Mode.newItem, null);
			}
		});
		
		deleteItemButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				for (ViewListener l : listeners) {
					if (getSelectedItem() != null)
						l.deleteItemButtonClick(getSelectedItem().getId());

				}

			}
		});

		addRandomItemButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				for (ViewListener l : listeners) {
					l.addRandomItemButtonClick();
				}
			}
		});

		editItemButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				openEditorWindow(Mode.editItem, getSelectedItem());
			}
		});
	}
	
	public void initItemList() {
		itemList.addContainerProperty(ID_COLUMN, Integer.class, null);
		itemList.addContainerProperty(NAME_COLUMN, String.class, null);
		itemList.addContainerProperty(DATE_COLUMN, Date.class, null);

		itemList.setSelectable(true);
		itemList.setImmediate(true);
		itemList.setWidth("100%");

		itemList.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					openEditorWindow(Mode.editItem, getMyItemFromItem(event.getItem()));
				}
			}
		});
		
		itemList.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(itemList.getValue() != null) {
					editItemButton.setEnabled(true);
					deleteItemButton.setEnabled(true);
				} else {
					editItemButton.setEnabled(false);
					deleteItemButton.setEnabled(false);
				}
			}
		});
	}
	
	private void openEditorWindow(Mode mode, MyItem item) {
		editWindow.center();
		editWindow.setMode(mode);
		if(mode == Mode.editItem) editWindow.setItem(item);
		UI.getCurrent().addWindow(editWindow);
	}

	public void setItems(List<MyItem> items) {
		itemList.removeAllItems();
		for (int i = 0; i < items.size(); i++) {
			MyItem item = items.get(i);
			itemList.addItem(new Object[] { new Integer(item.getId()), item.getName(), item.getDate() }, i);
		}
	}
	
	private MyItem getSelectedItem() {
		if (itemList.getValue() != null)
			return getMyItemFromItem(itemList.getItem(itemList.getValue()));
		else
			return null;
	}

	private MyItem getMyItemFromItem(Item item) {
		MyItem myItem = new MyItem();
		myItem.setId((Integer) item.getItemProperty(ID_COLUMN).getValue());
		myItem.setName((String) item.getItemProperty(NAME_COLUMN).getValue());
		myItem.setDate((Date) item.getItemProperty(DATE_COLUMN).getValue());
		return myItem;
	}

	@Override
	public void addListener(ViewListener listener) {
		listeners.add(listener);
		editWindow.addListener(listener);
	}

	@Override
	public void showError(String message) {
		Notification.show(Messages.getString("ERROR"), message, Notification.Type.ERROR_MESSAGE); //$NON-NLS-1$
	}

}
