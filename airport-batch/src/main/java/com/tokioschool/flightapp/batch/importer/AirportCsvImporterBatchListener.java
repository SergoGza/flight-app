package com.tokioschool.flightapp.batch.importer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class AirportCsvImporterBatchListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {

    log.info("Starting job {}", jobExecution.getJobInstance().getJobName());
  }

  @Override
  public void afterJob(JobExecution jobExecution) {

    if (jobExecution.getStatus() == BatchStatus.FAILED) {

      log.info(
          "Ending job: {} status: {} seconds: {}",
          jobExecution.getJobInstance().getJobName(),
          jobExecution.getStatus(),
          jobExecution.getExitStatus().getExitDescription());
    } else {

      log.info(
          "Ending job: {} status: {} seconds: {}",
          jobExecution.getJobInstance().getJobName(),
          jobExecution.getStatus(),
          ChronoUnit.SECONDS.between(jobExecution.getStartTime(), jobExecution.getEndTime()));
    }
  }
}
