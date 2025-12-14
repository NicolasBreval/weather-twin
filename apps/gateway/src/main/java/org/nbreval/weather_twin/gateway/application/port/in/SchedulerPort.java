package org.nbreval.weather_twin.gateway.application.port.in;

/**
 * Defines the entity which performs a measure's scheduler.
 * 
 * The scheduler manages some periodical functions, configurables
 * by the user, which executes a function to modify the aggregated
 * value for each sensor and interval and send them to external systems.
 */
public interface SchedulerPort {

  /**
   * Creates a new schedule for an specified interval, in milliseconds.
   * 
   * @param interval Interval in milliseconds to execute the scheduled task.
   */
  void schedule(long interval);

  /**
   * Removes the schedule task registered with specified interval.
   * 
   * @param interval Interval in milliseconds of the schedule task to remove.
   */
  void unschedule(long interval);
}
