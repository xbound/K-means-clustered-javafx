# K-means clustered demonstration in JavaFX

Simple MVC java application demonstrating work of [K-means clustering alghorithm](https://en.wikipedia.org/wiki/K-means_clustering).

## Overview

### Project structure:

   - `src/controller` folder contains `Controller` class -  "communication link" between `view` and `model`.
   Also `Controller` contains all code referenced to GUI elements in `src/view/sample.fxml` file.
   
   - `src/model` folder contains main core of program. `ClusterSolver` class which recalculates positions of groups' centers till they don't change their position.
   `Point` class is representation of each colored point on scene.
   
   - `src/view` folder which contains GUI in `sample.fxml` and `Main` class which `main` method launches the program.
 

### Workflow
After launching app choose amount of points and groups on scene by moving sliders labeled as 'Points' and 'Groups' and then click 'Generate' buttons on the right. You can also choose one of three ([EUCLID](https://en.wikipedia.org/wiki/Euclidean_distance),[MANHATTAN](https://en.wiktionary.org/wiki/Manhattan_distance),[CHEBYSHEV](https://en.wikipedia.org/wiki/Chebyshev_distance)) [distance functions](https://en.wikipedia.org/wiki/Metric_(mathematics)) as a rule by which each point will be clustered.
After that click 'Start' button to start clustering points on scene. If you want to stop simulation simply press 'Stop' button to stop algorithm and press 'Reset' button
to clear scene.

For the first step ,after pressing 'Start' button solver executes `assignPointsToClusters()` method which begin assigning (by coloring ) each point to its nearest group center. Next step solver recalculates new center for each group based on points' coordinates group contains. It continues repeating those steps as long as points changes their groups.

In `Point` class `setColor()` method checks if point's current color is different from new color. If so that means point changed its group and `setColor()` method returns `false` boolean value to solver. Solver summarise results of each `setColor()` operation using AND operator. If summary result is `false` it continue recalculating new centers' coordinates for each group.

![Alt text](https://media.giphy.com/media/3ov9jSvhrfzkVGNDAQ/giphy.gif)

### Built with:
    
   - [IntelijIDEA 2017.2](https://www.jetbrains.com/idea/)
   - [SceneBuilder 2.0](http://www.oracle.com/technetwork/java/javase/downloads/sb2download-2177776.html)