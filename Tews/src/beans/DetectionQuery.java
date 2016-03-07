package beans;

import org.joda.time.DateTime;

public class DetectionQuery {

	private DateTime dateCorpus = new DateTime();
	private Integer timeSlice = 0;
	private Integer numberEvent = 0;
	private Integer numberWord = 0;
	private Double weightWord = 0.0;
	private Double mergerThreshold = 0.0;

	public DateTime getDateCorpus() {
		return this.dateCorpus;
	}

	public void setDateCorpus(DateTime dateCorpus) {
		this.dateCorpus = dateCorpus;
	}

	public Integer getTimeSlice() {
		return this.timeSlice;
	}

	public void setTimeSlice(Integer timeSlice) {
		this.timeSlice = timeSlice;
	}

	public Integer getNumberEvent() {
		return this.numberEvent;
	}

	public void setNumberEvent(Integer numberEvent) {
		this.numberEvent = numberEvent;
	}

	public Integer getNumberWord() {
		return this.numberWord;
	}

	public void setNumberWord(Integer numberWord) {
		this.numberWord = numberWord;
	}

	public Double getWeightWord() {
		return this.weightWord;
	}

	public void setWeightWord(Double weightWord) {
		this.weightWord = weightWord;
	}

	public Double getMergerThreshold() {
		return this.mergerThreshold;
	}

	public void setMergerThreshold(Double mergerThreshold) {
		this.mergerThreshold = mergerThreshold;
	}

}
