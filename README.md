# Workout your tasks with WorkManager

Until now, in order to perform background work in Android, developers had to choose between multiple execution options. At Google I/O 2018, the Android team launched  [_WorkManager_](https://developer.android.com/topic/libraries/architecture/workmanager)  as part of the Android Jetpack.

This library provides a simple and clean interface to specify deferrable, asynchronous  tasks  and when they should run. This blog post is the first in a new series on  _WorkManager_. The series will include an overview about the Android memory model, existing background solutions, what’s happening behind the scenes and why and when we should use  _WorkManager_.

Also, we will discover details about the architecture of the  _WorkManager_  library and about the main components(_Worker_,  _WorkRequest_,  _WorkManager_,  _WorkInfo_) of it. Finally, we will highlight how to use  _WorkManager_  for scenarios like chained sequences of tasks that run in a specified order, unique named sequences, tasks that pass and return values and how to apply constraints in order to decide when to run the task.

 1. [Workout your tasks with WorkManager — Intro](https://proandroiddev.com/workout-your-tasks-with-workmanager-intro-db5aefe14d66)
 2. [Workout your tasks with WorkManager — Main Components](https://proandroiddev.com/workout-your-tasks-with-workmanager-main-components-1c0c66317a3e)
 3. [Workout your tasks with WorkManager — Advanced Topics](https://medium.com/@magdamiu/workout-your-tasks-with-workmanager-advanced-topics-c469581c235b)

![enter image description here](https://raw.githubusercontent.com/magdamiu/DemoWorkManager/master/WorkManager.png)
