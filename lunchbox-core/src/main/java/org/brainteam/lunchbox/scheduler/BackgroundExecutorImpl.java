package org.brainteam.lunchbox.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.brainteam.lunchbox.i18n.Translator;
import org.brainteam.lunchbox.jmx.BackgroundExecutorConfiguration;
import org.brainteam.lunchbox.jmx.ConfigurationListener;
import org.brainteam.lunchbox.json.JsonSchedulerJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackgroundExecutorImpl implements BackgroundExecutor, ConfigurationListener {

	static final String IMPORTMENU_NAME = "importmenu";
	static final String LOCKOFFERS_NAME = "lockoffers";
	static final String MAILBILLING_NAME = "mailbilling";
	static final String MAILORDER_NAME = "mailorder";
	static final String GROUP_WORKFLOW = "Workflow";
	
	private static final String TRIGGERNAME_IMPORTMENU = "Trigger-ImportMenu";
	private static final String TRIGGERNAME_LOCKOFFERS = "Trigger-LockOffer";
	private static final String TRIGGERNAME_MAILBILLING = "Trigger-MailBilling";
	private static final String TRIGGERNAME_MAILORDER = "Trigger-MailOrder";

	private Scheduler scheduler;
	
	private BackgroundExecutorConfiguration configuration;
	
	@Autowired
	private Translator translator;

	private JobDetail buildLockOffersJob() {
		return JobBuilder.newJob(LockOffersJob.class)
				.withIdentity(LOCKOFFERS_NAME, GROUP_WORKFLOW)
				.withDescription(getDescription(LOCKOFFERS_NAME))
				.build();
	}

	private JobDetail buildImportMenuJob() {
		return JobBuilder.newJob(ImportMenuJob.class)
				.withIdentity(IMPORTMENU_NAME, GROUP_WORKFLOW)
				.withDescription(getDescription(IMPORTMENU_NAME))
				.build();
	}
	
	private JobDetail buildMailBillingJob() {
		return JobBuilder.newJob(MailBillingJob.class)
				.withIdentity(MAILBILLING_NAME, GROUP_WORKFLOW)
				.withDescription(getDescription(MAILBILLING_NAME))
				.build();
	}

	private JobDetail buildMailOrderJob() {
		return JobBuilder.newJob(MailOrderJob.class)
				.withIdentity(MAILORDER_NAME, GROUP_WORKFLOW)
				.withDescription(getDescription(MAILORDER_NAME))
				.build();
	}

	@Override
	@PostConstruct
	public synchronized void start() {
		if (scheduler != null) {
			return;
		}
		
		startScheduler();
		scheduleJob(buildImportMenuJob(), buildImportMenuTrigger());
		scheduleJob(buildLockOffersJob(), buildLockOffersTrigger());
		scheduleJob(buildMailBillingJob(), buildMailBillingTrigger());
		scheduleJob(buildMailOrderJob(), buildMailOrderTrigger());
	}
	
	private void startScheduler() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			getScheduler().start();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	private void scheduleJob(JobDetail jobDetail, Trigger trigger) {
		try {
			getScheduler().scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	private Trigger buildImportMenuTrigger() {
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity(TRIGGERNAME_IMPORTMENU, GROUP_WORKFLOW)
				.withSchedule(CronScheduleBuilder.cronSchedule(getConfiguration().getCronScheduleImportMenu()))
				.build();
		return cronTrigger;
	}
	
	private Trigger buildLockOffersTrigger() {
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity(TRIGGERNAME_LOCKOFFERS, GROUP_WORKFLOW)
				.withSchedule(CronScheduleBuilder.cronSchedule(getConfiguration().getCronScheduleLockOffers()))
				.build();
		return cronTrigger;
	}
	
	private Trigger buildMailBillingTrigger() {
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity(TRIGGERNAME_MAILBILLING, GROUP_WORKFLOW)
				.withSchedule(CronScheduleBuilder.cronSchedule(getConfiguration().getCronScheduleMailBilling()))
				.build();
		return cronTrigger;
	}
	
	private Trigger buildMailOrderTrigger() {
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity(TRIGGERNAME_MAILORDER, GROUP_WORKFLOW)
				.withSchedule(CronScheduleBuilder.cronSchedule(getConfiguration().getCronScheduleMailOrder()))
				.build();
		return cronTrigger;
	}

	public void triggerJob(String group, String name) {
		try {
			List<String> groupNames = scheduler.getJobGroupNames();
			for (String groupName : groupNames) {
				Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupName));
				for (JobKey jobKey : jobKeys) {
					if (jobKey.getGroup().equals(group) && jobKey.getName().equals(name)) {
						scheduler.triggerJob(jobKey);
						return;
					}
				}
			}			
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@PreDestroy
	public void stop() {
		if (scheduler == null) {
			return;
		}
		
		stopScheduler();
		scheduler = null;
	}

	private void stopScheduler() {
		try {
			getScheduler().shutdown(true);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<JsonSchedulerJob> getJobInfo() {
		List<JsonSchedulerJob> jobs = new ArrayList<>();
		try {
			List<String> groupNames = scheduler.getJobGroupNames();
			for (String groupName : groupNames) {
				Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupName));
				for (JobKey jobKey : jobKeys) {
					jobs.add(toJson(jobKey));
				}
			}			
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
		return jobs;
	}

	private JsonSchedulerJob toJson(JobKey jobKey) throws SchedulerException {
		JsonSchedulerJob job = new JsonSchedulerJob();
		job.setKey(jobKey.getName());
		job.setGroup(jobKey.getGroup());
		job.setName(getTranslator().t("action." + jobKey.getName() + ".name"));

		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		job.setDescription(jobDetail.getDescription());

		List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
		if (triggersOfJob.size() == 1) {
			Trigger triggerOfJob = triggersOfJob.get(0);
			job.setLastExecution(triggerOfJob.getPreviousFireTime());
			job.setNextExecution(triggerOfJob.getNextFireTime());
		}
		return job;
	}
	
	protected String getDescription(String name) {
		return getTranslator().t("action." + name + ".description");
	}
	
	@Override
	public void configurationChanged(String key, String newValue) {
		stop();
		start();
	}
	
	public boolean isStarted() {
		return getScheduler() != null;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}
	
	protected BackgroundExecutorConfiguration getConfiguration() {
		return configuration;
	}
	
	protected Translator getTranslator() {
		return translator;
	}
	
	@Autowired
	public void setConfiguration(BackgroundExecutorConfiguration configuration) {
		if (this.configuration != null) {
			this.configuration.removeConfigurationListener(this);
		}
		this.configuration = configuration;
		if (this.configuration != null) {
			this.configuration.addConfigurationListener(this);
		}
	}

}
