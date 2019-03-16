package App.FXControllers;

import App.Classes.Location;
import App.Classes.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class OptionsList {
    /**
     * Creates an ObservableList of options to add to a dropdown menu
     * @param map
     * @return
     */
    public static ObservableList<String> createList(HashMap<String, String> map){
        Set s = map.keySet();
        Iterator it = s.iterator();
        ObservableList<String> options = FXCollections.observableArrayList();

        while(it.hasNext()){
            options.add((String) it.next());
        }

        return options;
    }

    /**
     * Crates an ObservableList of type names given a list of types
     * @param types A list of types
     * @return A list of type names
     */
    @SuppressWarnings("Duplicates")
    public static ObservableList<String> createTypeNameList(ObservableList<Type> types){
        ObservableList<String> names = FXCollections.observableArrayList();

        for(int i = 0; i < types.size(); i++){
            names.add(types.get(i).getName());
        }

        return names;
    }

    /**
     * Searches for a type in a list by the type's name
     * @param list The list of types to search
     * @param nameToFind The type name to search for
     * @return The type if found
     * @throws ListItemNotFoundException if type cannot be found
     */
    public static Type findTypeByName(ObservableList<Type> list, String nameToFind) throws ListItemNotFoundException {
        Type type = null;

        for(int i = 0; i < list.size(); i++){
            Type t = list.get(i);
            if(t.getName().equals(nameToFind)){
                type = t;
            }
        }

        if(type == null){
            throw new ListItemNotFoundException();
        }else {
            return type;
        }
    }

    /**
     * Crates an ObservableList of location names given a list of locations
     * @param locations A list of locations
     * @return A list of location names
     */
    @SuppressWarnings("Duplicates")
    public static ObservableList<String> createLocationNameList(ObservableList<Location> locations){
        ObservableList<String> names = FXCollections.observableArrayList();

        for(int i = 0; i < locations.size(); i++){
            names.add(locations.get(i).getName());
        }

        return names;
    }

    /**
     * Searches for a location in a list by the location's name     * @param list The list of locations to search
     * @param nameToFind The location name to search for
     * @return The location if found
     * @throws ListItemNotFoundException if location cannot be found
     */
    public static Location findLocationByName(ObservableList<Location> list, String nameToFind) throws ListItemNotFoundException {
        Location loc = null;

        for(int i = 0; i < list.size(); i++){
            Location l = list.get(i);
            if(l.getName().equals(nameToFind)){
                loc = l;
            }
        }

        if(loc == null){
            throw new ListItemNotFoundException();
        }else {
            return loc;
        }
    }
}
