package vswe.stevescarts.arcade.monopoly;

import java.util.ArrayList;

public class PropertyGroup {
	private ArrayList<Property> properties;

	public PropertyGroup() {
		properties = new ArrayList<>();
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	public void add(final Property property) {
		properties.add(property);
	}
}
