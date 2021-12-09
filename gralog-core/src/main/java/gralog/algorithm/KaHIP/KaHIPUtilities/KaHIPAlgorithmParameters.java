/* This file is part of Gralog, Copyright (c) 2016-2018 LaS group, TU Berlin.
 * License: https://www.gnu.org/licenses/gpl.html GPL version 3 or later. */
package gralog.algorithm.KaHIP.KaHIPUtilities;

import gralog.algorithm.StringAlgorithmParametersList;
import gralog.parser.DoubleSyntaxChecker;
import gralog.parser.IntSyntaxChecker;
import gralog.parser.SyntaxChecker;
import gralog.preferences.Preferences;

import java.util.*;

/**
 *
 */
public class KaHIPAlgorithmParameters extends StringAlgorithmParametersList {
    public KaHIPAlgorithmParameters(List<String> labels, List<String> initialValues, List<String> explanations) {
        super(initialValues);
        this.labels = labels;
        this.explanations = explanations;
    }
}

