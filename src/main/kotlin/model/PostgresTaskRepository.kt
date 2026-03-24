package com.kchat.model

import com.kchat.model.db.TaskDAO
import com.kchat.model.db.TaskTable
import com.kchat.model.db.daoToModel
import com.kchat.utils.withTransaction
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere

class PostgresTaskRepository : TaskRepository {
    override suspend fun allTasks(): List<Task> = withTransaction {
        TaskDAO.all().map(::daoToModel)
    }

    override suspend fun tasksByPriority(priority: Priority): List<Task> {
        return withTransaction {
            TaskDAO
                .find { (TaskTable.priority eq priority.toString()) }
                .map(::daoToModel)
        }
    }

    override suspend fun taskByName(name: String): Task? {
        return withTransaction {
            TaskDAO
                .find { (TaskTable.name eq name) }
                .limit(1)
                .map(::daoToModel)
                .firstOrNull()
        }
    }

    override suspend fun addTask(task: Task) {
        withTransaction {
            TaskDAO.new {
                name = task.name
                description = task.description
                priority = task.priority.toString()
            }
        }
    }

    override suspend fun removeTask(name: String): Boolean {
        return withTransaction {
            val rowsDeleted = TaskTable.deleteWhere {
                TaskTable.name eq name
            }
            rowsDeleted == 1
        }
    }
}