package App.FXControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class OptionsListCreator {
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
}
