from locust import HttpUser, task, between, LoadTestShape

class LocustUser(HttpUser):
    wait_time = between(3, 4)

    @task(1)
    def index(self):
        self.client.get('https://i-eum-u.com/')