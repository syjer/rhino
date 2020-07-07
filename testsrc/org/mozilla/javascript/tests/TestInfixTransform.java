/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mozilla.javascript.tests;

import junit.framework.TestCase;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * From @makusuko (Markus Sunela), imported from PR https://github.com/mozilla/rhino/pull/561
 */
public class TestInfixTransform extends TestCase {
    protected final Global global = new Global();

    public TestInfixTransform() {
        global.init(ContextFactory.getGlobal());
    }


    public void testAccessingJavaListIntegerValues() {

        assertEquals("test1test1", runScriptAsString("'test'+value+'test'+value", 1));

        //assertEquals("test11", runScriptAsString("var O = function(a,b) {this.v = a+b};var f = function (t){return new O('test'+t, t)};f(value).v;", 1));
    }



    private String runScriptAsString(String scriptSourceText, Object value) {
        return runScript(scriptSourceText, value, Context::toString);
    }

    private <T> T runScript(String scriptSourceText, Object value, Function<Object, T> convert) {
        return ContextFactory.getGlobal().call(context -> {
            Scriptable scope = context.initStandardObjects(global);
            scope.put("value", scope, Context.javaToJS(value, scope));
            return convert.apply(context.evaluateString(scope, scriptSourceText, "", 1, null));
        });
    }
}