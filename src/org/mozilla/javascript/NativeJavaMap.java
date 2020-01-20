/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript;

import java.util.Map;

/**
 * This class reflects Java Maps into the JavaScript environment.
 */
public class NativeJavaMap extends NativeJavaObject implements SymbolScriptable {

    private Map<?, ?> map;
    private Class<?> cls;

    public NativeJavaMap(Scriptable scope, Object map) {
        super(scope, map, map.getClass());
        Class<?> cl = map.getClass();
        if (!Map.class.isAssignableFrom(cl)) {
            throw new RuntimeException("Map expected");
        }
        this.map = (Map<?, ?>) map;
        this.cls = cl;
    }

    @Override
    public String getClassName() {
        return "JavaMap";
    }

    @Override
    public Object unwrap() {
        return map;
    }

    public static NativeJavaMap wrap(Scriptable scope, Object map) {
        return new NativeJavaMap(scope, map);
    }
}
