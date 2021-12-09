package gralog.algorithm.KaHIP.KaHIPUtilities;

import gralog.rendering.GralogColor;
import gralog.rendering.GralogGraphicsContext;
import gralog.rendering.shapes.Ellipse;
import gralog.rendering.shapes.RenderingShape;
import gralog.structure.Edge;
import gralog.structure.Structure;
import gralog.structure.Vertex;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class KaHIPUtil {
    private static final List<GralogGraphicsContext.LineType> edgeType = Arrays.asList(GralogGraphicsContext.LineType.PLAIN, GralogGraphicsContext.LineType.DASHED, GralogGraphicsContext.LineType.DOTTED);

    //Use the gralog structure to read the current working graph and generate metis format
    public List<Integer> generateMetisFormat(Structure s, String header, Collection<Vertex> vertices) throws IOException {
        final String workingDirectory = System.getProperty("user.dir");
        File file = new File(workingDirectory + "/KaHIP_results/myGraph.graph");
        file.getParentFile().mkdirs();
        FileWriter myWriter = new FileWriter(file);

        List<Integer> edgeIDs = new ArrayList<>();

        myWriter.write(header);
        try {
            AtomicInteger i = new AtomicInteger();
            i.getAndIncrement();
            for (Vertex v : vertices) {
                Collection<Vertex> n = v.getNeighbours();
                n.forEach((tempVertex) -> {
                    edgeIDs.add(s.getEdgeByVertexIds(v.getId(), tempVertex.getId()).getId());
//                    System.out.println(s.getEdgeByVertexIds(v.getId(), tempVertex.getId()).getId());
                    try {
                        myWriter.write(tempVertex.getId() + 1 + " ");
                        myWriter.write((s.getEdgeByVertexIds(v.getId(), tempVertex.getId()).weight).intValue() + " ");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Metis file format of the graph is successfully generated");
        } catch (IOException e) {
            System.out.println("Metis file format of the graph is not generated -> ");
            e.printStackTrace();
        }
        return edgeIDs;
    }

    //Read the output file generated on successful execution of the terminal command
    public String readOutputFile(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            return everything;
        }
    }

    //Execute a terminal command requested by a KaHIP algorithm
    public String executeCommand(String finalCommand) throws IOException, InterruptedException {

        Process proc = Runtime.getRuntime().exec(finalCommand);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        String Error;
        List<String> stdErrorMessage = new ArrayList<>();
        List<String> stdInputMessage = new ArrayList<>();
        while ((Error = stdError.readLine()) != null) {
            if (!Error.contains("*"))
                stdErrorMessage.add(Error + "\n");
            System.out.println(Error);
        }
        while ((Error = stdInput.readLine()) != null) {
            if (!Error.contains("*"))
                stdInputMessage.add(Error + "\n");
            System.out.println(Error);
        }


        return String.join(" ", stdErrorMessage) + "\n\n" + String.join(" ", stdInputMessage);
    }


    /*
    These methods are for multi-level graph partitions and node separators
     */
    //Using the block ids from the output file, generate vertex blocks
    public List<List<Integer>> generateParts(Structure s, Collection<Vertex> vertices, Collection<Edge> edges, String fileName, boolean separator) throws IOException {
        final String outputFileText = readOutputFile(fileName);
        String[] blocks = outputFileText.split("\n");
        List<List<Integer>> vertexBlocks = new ArrayList<>();
        List<GralogColor> randomColors = new ArrayList<>();

        for (int i = 0; i < blocks.length; i++) {
            randomColors.add(new GralogColor((int) (Math.random() * 0x1000000)));
            List<Integer> eachPart = new ArrayList<>();
            for (int j = 0; j < vertices.size(); j++) {
                if (i == Integer.parseInt(blocks[j])) {
                    eachPart.add(j);
                }
            }
            if (eachPart.size() != 0)
                vertexBlocks.add(eachPart);
        }
        List<Edge> requiredEdges = getRequiredEdges(s, vertexBlocks, randomColors);
        editNonRequiredEdges(s, requiredEdges, edges);
        movePartitionedPartsApart(vertices, vertexBlocks, randomColors, separator);

        return vertexBlocks;
    }

    //Get all the required edges so that the edge that are not required can be deleted
    public List<Edge> getRequiredEdges(Structure s, List<List<Integer>> vertexBlocks, List<GralogColor> randomColors) {
        List<Edge> requiredEdges = new ArrayList<>();
        Edge tempEdge;
        for (List<Integer> part : vertexBlocks) {
            for (int i = 0; i < part.size(); i++)
                for (int j = 0; j < part.size(); j++)
                    if (i - j > 0)
                        if (s.getEdgeByVertexIds(part.get(i), part.get(j)) != null) {
                            tempEdge = s.getEdgeByVertexIds(part.get(i), part.get(j));
                            if (vertexBlocks.indexOf(part) != vertexBlocks.size() - 1)
                                tempEdge.color = randomColors.get(vertexBlocks.indexOf(part));
                            else
                                tempEdge.color = GralogColor.BLACK;
                            tempEdge.thickness = 0.05;
                            requiredEdges.add(tempEdge);
                        }
        }
        return requiredEdges;
    }

    //Change the edge patterns
    public void editNonRequiredEdges(Structure s, List<Edge> requiredEdges, Collection<Edge> edges) {
        for (Edge e : edges) {
            e.label = "";
            if (!requiredEdges.contains(e)) {
                e.type = GralogGraphicsContext.LineType.DOTTED;
                e.color = GralogColor.GRAY;
                e.thickness = 0.02;
            }
        }
    }

    //Finally, move the vertex blocks apart
    public void movePartitionedPartsApart(Collection<Vertex> vertices, List<List<Integer>> vertexBlocks, List<GralogColor> randomColors, boolean separator) {
        for (Vertex v : vertices) {
            double xCoordinate = v.getCoordinates().getX();
            double yCoordinate = v.getCoordinates().getY();
            for (int i = 0; i < vertexBlocks.size(); i++) {
                if (vertexBlocks.get(i).contains(v.id)) {
                    if (separator) {
                        if (i != vertexBlocks.size() - 1) {
                            v.fillColor = randomColors.get(i);
                            v.setCoordinates(i * (vertices.size() + 5) + xCoordinate, yCoordinate);
                        } else {
                            v.fillColor = GralogColor.WHITE;
                            v.setCoordinates(xCoordinate + i * (vertices.size() / 2), yCoordinate - i * (vertices.size() / 2 + 2));
                        }
                    } else {
                        v.fillColor = randomColors.get(i);
                        v.setCoordinates(i * (vertices.size() + 5) + xCoordinate, yCoordinate);
                    }
                }
            }
        }
    }


    /*
    These methods are for edge partitions
    */
    //Using the block ids from the output file, generate edge blocks
    public List<List<Integer>> generateEdgeParts(Structure s, List<Integer> edges) throws IOException {
        final String outputFileText = readOutputFile("KaHIP_results/output");
        String[] blocks = outputFileText.split("\n");
        List<List<Integer>> edgeBlocks = new ArrayList<>();

        for (int i = 0; i < blocks.length; i++) {
            List<Integer> eachPart = new ArrayList<>();
            for (int j = 0; j < edges.size(); j++) {
                if (i == Integer.parseInt(blocks[j])) {
                    if (!eachPart.contains(edges.get(j)))
                        eachPart.add(edges.get(j));
                }
            }
            if (eachPart.size() != 0)
                edgeBlocks.add(eachPart);
        }
        editEdges(s, edgeBlocks);

        return edgeBlocks;
    }

    //Change the edge patters and colors using the edge blocks
    public void editEdges(Structure s, List<List<Integer>> edgeBlocks) {

        GralogGraphicsContext.LineType tempType;
        GralogColor tempColor;

        for (int i = 0; i < edgeBlocks.size(); i++) {
            tempType = edgeType.get(i % 3);
            tempColor = new GralogColor((int) (Math.random() * 0x1000000));
            for (int j = 0; j < edgeBlocks.get(i).size(); j++) {
                Edge e = s.getEdgeById((edgeBlocks.get(i)).get(j));
                e.color = tempColor;
                e.type = tempType;
                e.label = String.valueOf(i);
            }
        }
    }

    /*
    This method is used for Node Ordering
    */
    //Move the nodes around the circle in the sequence of node ordering from the output file
    public void moveNodesIntoCircle(Structure s) throws IOException {
        String outputFileText = readOutputFile("KaHIP_results/output");
        String[] blocks = outputFileText.split("\n");
        String[] order = new String[blocks.length - 1];
        for (int i = 0; i < blocks.length - 1; i++)
            order[i] = String.valueOf(Integer.parseInt(blocks[i + 1].split("\t")[1]) - 1);

        System.out.println(Arrays.toString(order));
        int n = order.length;
        Vertex tempVertex;
        double x = (0.5) / n;
        for (int i = 0; i < n; i++) {
            tempVertex = s.getVertexById(Integer.parseInt(order[i]));

            tempVertex.setCoordinates(
                    Math.sin(i * 2 * Math.PI / (n + 1)) * n / 2 + s.getVertexById(Integer.parseInt(order[0])).getCoordinates().getX(),
                    Math.cos(i * 2 * Math.PI / (n + 1)) * -n / 2 + s.getVertexById(Integer.parseInt(order[0])).getCoordinates().getX()
            );
        }

        tempVertex = s.getVertexById(Integer.parseInt(order[0]));
        tempVertex.fillColor = new GralogColor(200, 255, 200);
        tempVertex = s.getVertexById(Integer.parseInt(order[n - 1]));
        tempVertex.fillColor = new GralogColor(255, 200, 200);
    }

}