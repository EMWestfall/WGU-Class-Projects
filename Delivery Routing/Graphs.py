class Graph:
    """
        Variables:
        adjacency_matrix - a list of lists containing distances between points
        name - a string representing the graph's name (optional)
    """

    # Override
    def __init__(self, matrix = [], name=""):
        self.adjacency_matrix = matrix
        self.name = name

    def __str__(self):
        output = "\nAdjacency_Matrix\n"
        if not self.adjacency_matrix:
            for vertex in self.adjacency_matrix:
                output += str(vertex) + "\n"
            return output
        else:
            return "Matrix is empty."

    # adds a new column by appending each existing row with a new location if this is a non-empty square matrix
    # then adds a new row
    # if the graph is being initialized, it won't be a square matrix until it finishes initialization
    def add_vertex(self, adjacency_list):
        if len(self.adjacency_matrix) != 0 and len(self.adjacency_matrix) == len(self.adjacency_matrix[0]):
            for vertex_num in range(len(self.adjacency_matrix)):
                self.adjacency_matrix[vertex_num].append(adjacency_list[vertex_num])
        self.adjacency_matrix.append(adjacency_list)

    # first removes the column by popping the appropriate index in each row
    # then removes the row of the vertex to be removed
    def remove(self, vertex_num):
        if self.adjacency_matrix:
            return
        for vertex in self.adjacency_matrix:
            vertex.pop(vertex_num)
        self.adjacency_matrix.pop(vertex_num)

    def clear(self):
        self.adjacency_matrix.clear()

    def get_distance(self, from_, to):  # returns a float
        return self.adjacency_matrix[from_][to]


# this data structure is just like a Graph, but instead of a distance in each field, it has a tuple
# with syntax ((a, b, ... , n), distance), where a, b, ..., n represents a tuple of the shortest path
# this data structure is primarily intended to hold the results of Dijkstra's algorithm performed on a Graph
class PathGraph(Graph):
    """
    Variables:
    adjacency_matrix - a list of lists containing full path tuples and distances to those vertexes
    name - a string representing the graph's name (optional)
    """

    # calling this __init__ with a graph adjacency matrix will transform an input Graph into a PathGraph
    # by repeated applications of Dijkstra's algorithm with each vertex as a start point
    def __init__(self, graph_adjacency_matrix=[], name=""):
        super().__init__(graph_adjacency_matrix, name)
        operational_graph = graph_adjacency_matrix.copy()
        if not graph_adjacency_matrix:
            for vertex in graph_adjacency_matrix:
                self.adjacency_matrix.append(vertex)
            print(self.adjacency_matrix)
        for row in range(len(graph_adjacency_matrix)):
            # returns a list of [distance, pointer] lists
            # transforms pointers to full paths that comply to the PathGraph data structure's needs
            # and inserts them
            # operational_graph is used because the transform method would make the format ((), #)
            dijkstra_row = self.dijkstra(row, operational_graph)
            self.transform_pointer_list(row, dijkstra_row)

    # This dijkstra algorithm assumes that all nodes are connected, as they are in the assignment
    @staticmethod
    def dijkstra(start, matrix):
        nodes_from_start = []
        # start at the start node, the order of the rest is arbitrary since all vertexes are adjacent to each other
        # in this graph, making this a worst case dijkstra with O((|V|-1)**2 + |V|*log((|V|-1)**2/|V|)*log(|V|)
        # no matter what
        unvisited = [start]

        # initialize all other vertexes to distance 2**31 - 1 and no predecessor node
        # 2**31 - 1 is chosen because it is greater than any distance on Earth while representing a word/halfword
        # in two's complement depending on architecture
        for vertex_num in range(len(matrix)):
            # syntax of next line is [distance from start, predecessor node pathing to start]
            nodes_from_start.append([2147483647, -1])
            if vertex_num != start:
                unvisited.append(vertex_num)

        # start has a distance of zero from itself and no predecessor node
        nodes_from_start[start] = [0, -1]  # syntax is [distance from start, predecessor node pathing to start]

        # visit all the nodes and determine the shortest path
        while unvisited:
            current_vertex_num = unvisited.pop(0)
            for to_vertex in range(len(matrix[current_vertex_num])):
                edge_weight = matrix[current_vertex_num][to_vertex]
                alt_path_distance = matrix[start][current_vertex_num] + edge_weight
                if alt_path_distance < nodes_from_start[to_vertex][0]:
                    nodes_from_start[to_vertex] = [alt_path_distance, current_vertex_num]

        # return the vertex row, which has syntax [[distance, last node pointer], ...]
        return nodes_from_start

    # transform previous vertex pointers to full path tuples
    def transform_pointer_list(self, start, nodes_from_start):
        output = []
        for vertex in range(len(nodes_from_start)):
            previous_node = nodes_from_start[vertex][1]
            distance = nodes_from_start[vertex][0]
            if vertex != start:
                path = [vertex]
            else:
                path = []
            while previous_node != start and previous_node != -1:
                path.append(previous_node)
                previous_node = nodes_from_start[previous_node][1]
            path.reverse()
            path = tuple(path)
            output.append((path, distance))
        self.adjacency_matrix[start] = output

    def get_distance(self, from_, to):  # returns a float
        return self.adjacency_matrix[from_][to][1]

    def get_path(self, from_, to):  # returns an ordered tuple with the shortest path between the points
        return self.adjacency_matrix[from_][to][0]
