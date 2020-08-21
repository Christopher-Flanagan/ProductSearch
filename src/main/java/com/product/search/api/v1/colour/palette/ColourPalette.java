package com.product.search.api.v1.colour.palette;

import com.product.search.api.v1.colour.Colour;

import java.util.List;

public interface ColourPalette {
    List<Colour> getPalette();
    void setPalette(List<Colour> palette);
}
