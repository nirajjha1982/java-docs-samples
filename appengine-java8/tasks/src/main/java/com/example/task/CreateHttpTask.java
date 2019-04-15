/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.task;

import com.google.cloud.tasks.v2beta3.CloudTasksClient;
import com.google.cloud.tasks.v2beta3.HttpMethod;
import com.google.cloud.tasks.v2beta3.HttpRequest;
import com.google.cloud.tasks.v2beta3.QueueName;
import com.google.cloud.tasks.v2beta3.Task;
import com.google.common.base.Strings;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;

import java.nio.charset.Charset;
import java.time.Clock;
import java.time.Instant;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CreateHttpTask {

  public static void main(String[] args) throws Exception {
    String projectId = System.getenv("PROJECT_ID");
    String queueName = System.getenv("QUEUE_ID");
    String location = System.getenv("LOCATION_ID");
    String url = System.getenv("URL");

    // [START cloud_tasks_create_http_task]
    // Instantiates a client.
    try (CloudTasksClient client = CloudTasksClient.create()) {

      // Variables provided by the CLI.
      // projectId = "my-project-id";
      // queueName = "my-appengine-queue";
      // location = "us-central1";
      // url = "https://<project-id>.appspot.com/tasks/create";
      String payload = "hello";

      // Construct the fully qualified queue name.
      String queuePath = QueueName.of(projectId, location, queueName).toString();

      // Construct the task body.
      Task.Builder taskBuilder = Task
          .newBuilder()
          .setHttpRequest(HttpRequest.newBuilder()
              .setBody(ByteString.copyFrom(payload, Charset.defaultCharset()))
              .setUrl(url)
              .setHttpMethod(HttpMethod.POST)
              .build());

      // Send create task request.
      Task task = client.createTask(queuePath, taskBuilder.build());
      System.out.println("Task created: " + task.getName());
    }
    // [END cloud_tasks_create_http_task]
  }
}
