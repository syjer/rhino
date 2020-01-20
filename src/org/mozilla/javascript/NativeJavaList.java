/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript;

import java.util.List;

/**
 * This class reflects Java Lists into the JavaScript environment.
 *
 * Based on https://github.com/mozilla/rhino/pull/561 by @makusuko
 *
 * @author Markus Sunela
 */
public class NativeJavaList extends NativeJavaObject implements SymbolScriptable {

    private List<?> list;
    private Class<?> cls;

    public NativeJavaList(Scriptable scope, Object list) {
        super(scope, list, list.getClass());
        Class<?> cl = list.getClass();
        if (!List.class.isAssignableFrom(cl)) {
            throw new RuntimeException("List expected");
        }
        this.list = (List) list;
        this.cls = cl;
    }

    @Override
    public String getClassName() {
        return "JavaList";
    }

    @Override
    public Object unwrap() {
        return list;
    }

    public static NativeJavaList wrap(Scriptable scope, Object array) {
        return new NativeJavaList(scope, array);
    }
}
