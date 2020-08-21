package com.product.search.api.v1.colour.palette;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.product.search.api.v1.colour.Colour;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@NoArgsConstructor
public class ImgixHexPalette implements ColourPalette {
    private List<Colour> colours = new ArrayList<>();

    @Override
    public List<Colour> getPalette() {
        return colours;
    }

    @Override
    public void setPalette(List<Colour> palette) {
        this.colours = palette;
    }

    @JsonProperty("colors")
    private void unpackNested(LinkedHashMap[] colours) {
        for (LinkedHashMap map: colours) {
            if(map.containsKey("hex")) {
                this.colours.add(new Colour((String) map.get("hex"), "hex"));
            }
        }
    }
}
