package org.datacite.conres.view;

import org.datacite.conres.model.Metadata;

public interface Transformer {
    Object transform(Metadata mr);
}
