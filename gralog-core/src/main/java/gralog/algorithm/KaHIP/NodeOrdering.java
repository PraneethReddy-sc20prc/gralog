/* This file is part of Gralog, Copyright (c) 2016-2018 LaS group, TU Berlin.
 * License: https://www.gnu.org/licenses/gpl.html GPL version 3 or later. */
package gralog.algorithm.KaHIP;

import gralog.algorithm.Algorithm;
import gralog.algorithm.AlgorithmDescription;
import gralog.algorithm.AlgorithmParameters;
import gralog.algorithm.KaHIP.KaHIPUtilities.KaHIPAlgorithmParameters;
import gralog.algorithm.KaHIP.KaHIPUtilities.KaHIPPreferences;
import gralog.algorithm.KaHIP.KaHIPUtilities.KaHIPUtil;
import gralog.progresshandler.ProgressHandler;
import gralog.structure.Edge;
import gralog.structure.Highlights;
import gralog.structure.Structure;
import gralog.structure.Vertex;

import java.util.*;

/**
 *
 */
@AlgorithmDescription(
        name = "KAHIP - Node Ordering",
        text = "Apply multi-level graph partitioning algorithms and my other graph functionalities",
        url = ""
)
public class NodeOrdering extends Algorithm {

    private static final List<String> requiredParameters = Arrays.asList("seed", "preconfiguration", "reduction_order");

    @Override
    public AlgorithmParameters getParameters(Structure s, Highlights highlights) {

        KaHIPPreferences p = new KaHIPPreferences();
        Map<String, List<String>> finalParameters = p.initializeParameters(this.getClass(), requiredParameters);

        return new KaHIPAlgorithmParameters(finalParameters.get("labels"), finalParameters.get("initialValues"), finalParameters.get("explanations"));
    }

    public Object run(Structure s, AlgorithmParameters params, Set<Object> selection, ProgressHandler onprogress) throws Exception {

        if (s.getVertices().size() == 0)
            return ("The structure should not be empty.");

        Collection<Vertex> vertices = s.getVertices();
        Collection<Edge> edges = s.getEdges();

        KaHIPUtil kaHIPUtil = new KaHIPUtil();

        kaHIPUtil.generateMetisFormat(s, vertices.size() + " " + edges.size() + " 1\n", vertices);

        final String workingDirectory = System.getProperty("user.dir") + "/KaHIP-master/deploy/";

        List<String> command = new ArrayList<>();
        command.add(workingDirectory);
        command.add("./node_ordering KaHIP_results/myGraph.graph ");
        command.add(" --output_filename=KaHIP_results/output");

        final String commandOutput = kaHIPUtil.executeCommand(String.join("", command));

        if(commandOutput.split("\n\n").length >1 && commandOutput.split("\n\n")[0].isEmpty())
            kaHIPUtil.moveNodesIntoCircle(s);

        return commandOutput + "\nNodes are ordered starting with color Green and ends with color Red in Clockwise direction";
    }
}
