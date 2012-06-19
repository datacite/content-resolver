package org.datacite.conres.view;

import org.datacite.conres.model.Model;

public interface Transformer {
    Object transform(Model mr);
}
