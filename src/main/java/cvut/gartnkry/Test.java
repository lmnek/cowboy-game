package cvut.gartnkry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import cvut.gartnkry.model.map.Map;

public class Test {
    private String test;
    private Map map;

    public void setTest(String test) {
        this.test = test;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
