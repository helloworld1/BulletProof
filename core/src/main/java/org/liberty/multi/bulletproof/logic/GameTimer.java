package org.liberty.multi.bulletproof.logic;

import com.badlogic.gdx.utils.ArrayMap;

/**
 * The home made timer to deal with Libgdx timer threading issue.
 *
 * The methods in the class should only be used in the GL thread.
 * They are not thread safe.
 */
public class GameTimer {

    private float currentTime = 0.0f;

    private boolean started = false; 

    /**
     * Map from task to the time to run.
     */
    private ArrayMap<Runnable, Float> taskStartTimeMap= new ArrayMap<Runnable, Float>();

    /**
     * Map from task to its interval.
     */
    private ArrayMap<Runnable, Float> taskIntervalmap= new ArrayMap<Runnable, Float>();

    /**
     * Constructor.
     */
    public GameTimer() {
        start();
    }

    /**
     * start the timer.
     */
    public void start() {
        started = true;
    }

    /**
     * Stop the timer.
     */
    public void stop() {
        started = false;
    }
    /**
     * Clear all tasks.
     */
    public void clear() {
        taskStartTimeMap.clear();
        taskIntervalmap.clear();
    }

    /**
     * Schedule a task to run.
     *
     * @param task the task to run
     * @param delay the delay. If it is small number or negative,
     * the task will run immdiately in the next frame.
     */
    public void schedule(Runnable task, float delay) {
        taskStartTimeMap.put(task, currentTime + delay);
    }

    /**
     * Schedule a task to run.
     *
     * @param task the task to run
     * @param delay the delay. If it is small number or negative,
     * the task will run immdiately in the next frame.
     * @param interval the interval of the task
     */
    public void schedule(Runnable task, float delay, float interval) {
        taskStartTimeMap.put(task, currentTime + delay);
        taskIntervalmap.put(task, interval);
    }

    /**
     * Must be called in the render().
     */
    public void update(float delta) {
        if (!started) {
            return;
        }
        currentTime += delta;
        for (Runnable task : taskStartTimeMap.keys()) {
            float timeToRun = taskStartTimeMap.get(task);

            if (currentTime >= timeToRun) {
                task.run();
                taskStartTimeMap.removeKey(task);
                // If the task has interval
                if (taskIntervalmap.containsKey(task)) {
                    taskStartTimeMap.put(task, currentTime + taskIntervalmap.get(task));
                }
            }
        }
    }
}

