package com.example.crudproject.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.crudproject.model.MyItem;
import com.example.crudproject.view.CRUDView.ViewListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MyItemEditorWindow extends Window {
	public enum Mode {
		newItem, editItem;
	}

	private Mode mode;
	private MyItem item = new MyItem();

	private List<ViewListener> listeners = new ArrayList<ViewListener>();
	private CRUDViewImpl view;

	private Label idLabel = new Label();
	private TextField nameField = new TextField(Messages.getString("NAME_FIELD"));
	private DateField dateField = new DateField(Messages.getString("DATE_FIELD"));
	private Button addButton = new Button(Messages.getString("APPLY"));
	private Button cancelButton = new Button(Messages.getString("CANCEL"));

	public MyItemEditorWindow(CRUDViewImpl view) {
		setModal(true);
		this.view = view;
		initLayout();
		initButtons();
		nameField.setImmediate(true);
		dateField.setImmediate(true);
	}

	private void initLayout() {
		VerticalLayout layout = new VerticalLayout();
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);

		FieldGroup fields = new FieldGroup();
		fields.bind(nameField, Messages.getString("NAME_FIELD"));
		fields.bind(dateField, Messages.getString("DATE_FIELD"));

		idLabel.setCaption(Messages.getString("ID_FIELD"));
		formLayout.addComponent(idLabel);
		formLayout.addComponent(nameField);
		formLayout.addComponent(dateField);

		nameField.setWidth("100%");
		dateField.setWidth("100%");
		dateField.setResolution(Resolution.SECOND);

		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(cancelButton);
		buttonsLayout.addComponent(addButton);
		buttonsLayout.setMargin(true);

		layout.addComponent(formLayout);
		layout.addComponent(buttonsLayout);
		layout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);

		this.setContent(layout);
	}

	private void initButtons() {
		cancelButton.setClickShortcut(KeyCode.ESCAPE);
		addButton.setClickShortcut(KeyCode.ENTER);

		cancelButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});

		addButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (validate()) {
					item.setName(nameField.getValue());
					item.setDate(dateField.getValue());
					for (ViewListener l : listeners) {
						if (mode == mode.newItem)
							l.addItemButtonClick(item);
						else
							l.editItemButtonClick(item);
					}
					close();
				} else {
					view.showError(Messages.getString("FIELDS_INVALID"));
				}
			}
		});
	}

	private boolean validate() {
		boolean valid = true;
		if (nameField.getValue().isEmpty())
			valid = false;

		return valid;
	}

	public void setItem(MyItem item) {
		this.item = item;
		idLabel.setValue("" + item.getId());
		nameField.setValue(item.getName());
		dateField.setValue(item.getDate());
	}

	public void addListener(ViewListener listener) {
		listeners.add(listener);
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		if (mode == Mode.editItem) {
			idLabel.setVisible(true);
			setCaption(Messages.getString("EDIT_ITEM_PROPERTIES"));
		} else if (mode == Mode.newItem) {
			idLabel.setVisible(false);
			nameField.setValue("");
			dateField.setValue(new Date());
			setCaption(Messages.getString("ADD_NEW_ITEM"));
		}
		nameField.focus();
	}
}
