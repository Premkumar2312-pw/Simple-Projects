import * as Notifications from 'expo-notifications';

export const scheduleTaskNotification = async (task) => {
  try {
    // Cancel existing notification if any
    if (task.notificationId) {
      await Notifications.cancelScheduledNotificationAsync(task.notificationId);
    }

    // Create notification date/time
    const taskDate = new Date(task.dueDate);
    const [hours, minutes] = task.dueTime.split(':');
    taskDate.setHours(parseInt(hours), parseInt(minutes), 0, 0);

    // Only schedule if the task is in the future and not completed
    if (taskDate > new Date() && !task.completed) {
      const notificationId = await Notifications.scheduleNotificationAsync({
        content: {
          title: `📚 ${task.type}: ${task.title}`,
          body: task.description || `Don't forget about your ${task.type.toLowerCase()}!`,
          sound: true,
          data: { taskId: task.id },
        },
        trigger: {
          date: taskDate,
          repeats: false,
        },
      });

      return notificationId;
    }

    return null;
  } catch (error) {
    console.error('Error scheduling notification:', error);
    return null;
  }
};

export const cancelTaskNotification = async (notificationId) => {
  try {
    if (notificationId) {
      await Notifications.cancelScheduledNotificationAsync(notificationId);
      return true;
    }
    return false;
  } catch (error) {
    console.error('Error canceling notification:', error);
    return false;
  }
};

export const scheduleRecurringReminder = async (task) => {
  try {
    // Cancel existing notification if any
    if (task.notificationId) {
      await Notifications.cancelScheduledNotificationAsync(task.notificationId);
    }

    // Don't schedule for completed tasks
    if (task.completed) {
      return null;
    }

    // Create notification date/time
    const taskDate = new Date(task.dueDate);
    const [hours, minutes] = task.dueTime.split(':');
    
    // Schedule daily reminder at the task time until due date
    const now = new Date();
    const reminderDate = new Date();
    reminderDate.setHours(parseInt(hours), parseInt(minutes), 0, 0);
    
    // If today's reminder time has passed, start from tomorrow
    if (reminderDate <= now) {
      reminderDate.setDate(reminderDate.getDate() + 1);
    }

    // Only schedule if reminder date is before or on due date
    if (reminderDate <= taskDate) {
      const notificationId = await Notifications.scheduleNotificationAsync({
        content: {
          title: `🔔 Daily Reminder: ${task.title}`,
          body: `${task.type} due on ${taskDate.toLocaleDateString()}`,
          sound: true,
          data: { taskId: task.id, isRecurring: true },
        },
        trigger: {
          hour: parseInt(hours),
          minute: parseInt(minutes),
          repeats: true,
        },
      });

      return notificationId;
    }

    return null;
  } catch (error) {
    console.error('Error scheduling recurring reminder:', error);
    return null;
  }
};

export const getAllScheduledNotifications = async () => {
  try {
    const notifications = await Notifications.getAllScheduledNotificationsAsync();
    return notifications;
  } catch (error) {
    console.error('Error getting scheduled notifications:', error);
    return [];
  }
};

export const cancelAllNotifications = async () => {
  try {
    await Notifications.cancelAllScheduledNotificationsAsync();
    return true;
  } catch (error) {
    console.error('Error canceling all notifications:', error);
    return false;
  }
};