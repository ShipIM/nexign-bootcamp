package com.example.nexign.api.source;

import java.util.List;

public interface Source<T> {

    List<T> provide();

}
