package com.example.test

import com.example.test.model.entity.Task
import com.example.test.repository.TaskRepository
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestApplicationTests() {
    @LocalServerPort
    private val port: Int? = null

    @Autowired
    var taskRepository: TaskRepository? = null

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port"
        taskRepository!!.deleteAll()
    }

    @AfterEach
    fun drop() {
        RestAssured.baseURI = "http://localhost:$port"
        taskRepository!!.deleteAll()
    }

    companion object {
        private val postgres = PostgreSQLContainer<Nothing>("postgres:latest")

        @BeforeAll
        fun beforeAll() {
            postgres.start()
        }

        @AfterAll
        fun afterAll() {
            postgres.stop()
            postgres.close()
        }

        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.getJdbcUrl() }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
        }
    }

    @Test
    fun deleteRequest() {
        val tasks = listOf(
            Task(
                title = "task1", description = "task2", creationDate = LocalDate.now(),
                modificationDate = null, isCompleted = false
            ),
            Task(
                title = "task2", description = "task2", creationDate = LocalDate.now(),
                modificationDate = null, isCompleted = false
            )
        )
        taskRepository?.saveAll(tasks)
        val testTask = taskRepository?.findTaskByTitle("task1")
        given()
            .contentType(ContentType.JSON)
            .`when`()
            .delete("/tasks/" + testTask!!.id)
            .then()
            .statusCode(200)
    }

    @Test
    fun createRequest() {
        val requestBody = """{ "title": "testTask1",
                                "description": "testDescription1"
                              }"""

        val response = given()
            .contentType(ContentType.JSON)
            .`when`()
            .body(requestBody)
            .post("/tasks")
            .then()
            .extract()
            .response()

        val testTask = taskRepository?.findTaskByTitle("testTask1")

        Assertions.assertEquals(200, response.statusCode())
        Assertions.assertEquals("testTask1", response.jsonPath().getString("title"))
        Assertions.assertEquals("testDescription1", response.jsonPath().getString("description"))
        Assertions.assertEquals(LocalDate.now().toString(), response.jsonPath().getString("creationDate"))
        Assertions.assertEquals(null, response.jsonPath().getString("modificationDate"))
        Assertions.assertEquals(false, response.jsonPath().getBoolean("isCompleted"))
        Assertions.assertEquals(testTask!!.id, response.jsonPath().getLong("id"))
    }

    @Test
    fun updateRequest() {
        val task = Task(title = "task1", description = "task2", creationDate = LocalDate.now(),
            modificationDate = null, isCompleted = false)
        taskRepository?.save(task)

        val testTask = taskRepository?.findTaskByTitle("task1")

        val requestBody = """{ "title": "updateTestTitle",
                                "description": "updateTestDescription"
                              }"""

        val response = given()
            .contentType(ContentType.JSON)
            .`when`()
            .body(requestBody)
            .patch("/tasks/${testTask?.id}")
            .then()
            .extract()
            .response()

        Assertions.assertEquals(200, response.statusCode())
        Assertions.assertEquals("updateTestTitle", response.jsonPath().getString("title"))
        Assertions.assertEquals("updateTestDescription", response.jsonPath().getString("description"))
        Assertions.assertEquals(LocalDate.now().toString(), response.jsonPath().getString("creationDate"))
        Assertions.assertEquals(LocalDate.now().toString(), response.jsonPath().getString("modificationDate"))
        Assertions.assertEquals(false, response.jsonPath().getBoolean("isCompleted"))
        Assertions.assertEquals(testTask!!.id, response.jsonPath().getLong("id"))
    }

    @Test
    fun completeRequest() {
        val task = Task(title = "task1", description = "description1", creationDate = LocalDate.now(),
            modificationDate = null, isCompleted = false)
        taskRepository?.save(task)

        val testTask = taskRepository?.findTaskByTitle("task1")

        val response = given()
            .contentType(ContentType.JSON)
            .`when`()
            .put("/tasks/${testTask?.id}/complete")
            .then()
            .extract()
            .response()

        Assertions.assertEquals(200, response.statusCode())
        Assertions.assertEquals("task1", response.jsonPath().getString("title"))
        Assertions.assertEquals("description1", response.jsonPath().getString("description"))
        Assertions.assertEquals(LocalDate.now().toString(), response.jsonPath().getString("creationDate"))
        Assertions.assertEquals(LocalDate.now().toString(), response.jsonPath().getString("modificationDate"))
        Assertions.assertEquals(true, response.jsonPath().getBoolean("isCompleted"))
        Assertions.assertEquals(testTask!!.id, response.jsonPath().getLong("id"))
    }
}

