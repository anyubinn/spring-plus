import random

from locust import HttpUser, task, tag, between

class UserPerformanceTest(HttpUser):
    wait_time = between(1, 5)

    @tag("read_user_by_nickname")
    @task
    def read_user(self):
        nicknames = ["test1", "target_nickname_1234", "user_08321cb6", "user_de12c7d2", "user_a3ecf499"]

        selected_nickname = random.choice(nicknames)

        self.client.get(url="/users", params={"nickname": selected_nickname}, name="/users?nickname=[nickname]")