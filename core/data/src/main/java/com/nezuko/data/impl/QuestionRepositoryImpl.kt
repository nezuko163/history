package com.nezuko.data.impl

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.nezuko.data.di.Dispatcher
import com.nezuko.data.di.MyDispatchers
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.repository.QuestionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class QuestionRepositoryImpl @Inject constructor(
    private val db: FirebaseDatabase,
    @Dispatcher(MyDispatchers.IO) private val IODispatcher: CoroutineDispatcher,
) : QuestionRepository {
    private val TAG = "QuestionRepositoryImpl"
    private val questionsRef = db.getReference("questions")

    override suspend fun getAllQuestions() = withContext(IODispatcher) {
        suspendCancellableCoroutine { continuation ->
            questionsRef.get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val a = snapshot.children.mapNotNull { dataSnapshot ->
                            dataSnapshot.getValue(QuestionModel::class.java)
                        }
                        Log.i(TAG, "getAllQuestions: $a")
                        continuation.resume(ArrayList(a))
                    } else {
                        continuation.resumeWithException(RuntimeException("снапшот нет("))
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    continuation.resumeWithException(e)
                }
                .addOnCanceledListener {
                    continuation.cancel()
                }
        }
    }

    override suspend fun insertQuestion(questionModel: QuestionModel) = withContext(IODispatcher) {
        suspendCancellableCoroutine<QuestionModel> { continuation ->
            val questionRef = db.getReference("questions").push()
            questionRef.setValue(questionModel.setId(questionRef.key!!))
                .addOnSuccessListener {
                    continuation.resume(questionModel)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    continuation.resumeWithException(e)
                }
                .addOnCanceledListener {
                    continuation.cancel()
                }
        }
    }


    override suspend fun generateQuestions(theme: String, count: Int) =
        withContext(IODispatcher) {
            Log.i(TAG, "getQuestions: start")
            suspendCancellableCoroutine<List<QuestionModel>> { continuation ->
                val questionTask: Task<DataSnapshot>
                Log.i(TAG, "getQuestions: suspe")
                db.getReference("questions").apply {
                    if (theme == "ALL") {
                        questionTask = get()
                    } else {
                        questionTask = equalTo(theme).get()
                    }
                }

                questionTask
                    .addOnSuccessListener { task ->
                        val allQuestions = task.children.mapNotNull { dataSnapshot ->
                            dataSnapshot.getValue(QuestionModel::class.java)
                        }

                        continuation.resume(allQuestions.shuffled().take(count))
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        continuation.resumeWithException(e)
                    }
                    .addOnCanceledListener {
                        continuation.cancel()
                    }
            }
        }
}