# SYSC4005_ProductionSim

# RUNNING THE PROGRAM

To run the program import it into a java IDE such as Eclipse. run the SimulationRunner class's main function. It will likely be seen that errors are popping up in the test package, this is because the JUnit libaray cannot be packaged with the program when we submit it. This is easily fixed by adding JUnit4 to the build path. The chosen number of replications is 117, so if R is unchanged it will take some time to finish running. Once it has completed, 117 .txt files will be created within the Results folder, showing detailed results and statistics taken for each replication. And in the console window it will be seen at the bottom what the three main long-run mean statistics and their confidence intervals are.

# PROGRAM FILES

The simulation is made up of 6 classes. The Component class creates objects that represent the individual components that travel through the simulation. The Event class creates objects that represent the Finish Inspection and End Assembly events that occur in the simulation. The FileEditor class creates and edits the .txt files that contain the statistics and details of each replication. The Model class contains the code that processes Events to run the simulation. The RandomNumberGenerator class creates objects that use our random number generators to generate random numbers. Lastly, the SimulationRunner class runs multiple replications of the simulation, determines what the across replications statistics would be, and prints them to the console.
