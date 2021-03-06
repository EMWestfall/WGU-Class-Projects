# WGU-Class-Projects
Class projects from my junior and senior year at Western Governor's University.

**Contents**

**Inventory (Software I)**

This was a first class in Java. The project was to build a simple inventory tracking system. I used the Ant building tool, the Netbeans IDE, and I spent a lot of effort learning JavaFX's tableviews, which use factories to generate the table data. The primary thing I learned from this project was the importance of structuring software in an organized design pattern, which I didn't really do well here and it cost me a lot of grief.

**Scheduler (Software II)**

This was a second class in Java. The project was to build a simple scheduling program that could schedule meetings with clients. I used the Maven building tool here and the Netbeans IDE. I learned a lot from my first Java class and used a much more organized design structure here. Notably, I used MVC. For the model, I used a DAO design pattern so the data itself was organized. This project also included Javadoc documentation. Although the interface is very simple, I consider the backend of this project to be much better than anything I produced before. It was far more organized and easier to work with.

**Delivery Routing (Data Structures and Algorithms II)**

This was from a second class in Data Stuctures and Algorithms. I enjoyed this class a lot. The project was to solve a complex version of the truck routing problem (most variants of the problem were thrown in: capacitance, deadlines, some packages had to be on the same truck, multiple returns to the hub were required, and there were multiple trucks). This project was built with Python using PyCharm. My strategy was to use k-medoids clustering to cluster the package destinations (I was given a distance matrix, not coordinates, so k-medoids had to be used rather than k-means) and then use greedy lookahead to route the trucks while placing deadline restrictions on the route permutation space. Alpha-beta pruning was heavily used on the routing algorithm's permutation space to drastically reduce iterations needed. Although I started off with a very organized and measured approach, in the end I think the organizational structure of the project could have been better.
