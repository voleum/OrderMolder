package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import dev.voleum.ordermolder.models.Document;

public class DocListItemViewModel extends BaseObservable {

    private Document doc;

    public DocListItemViewModel(Document doc) {
        this.doc = doc;
    }

    @Bindable
    public String getDocTitle() {
        return doc.toString();
    }
}
