package com.chatgpt.dto;

import java.util.List;
public class ChatGptResponse {

    private List<Choice> choices;

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}

	public ChatGptResponse() {
	}

    public static class Choice {

        private int index;
        private Message message;


		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public Message getMessage() {
			return message;
		}
		public void setMessage(Message message) {
			this.message = message;
		}
        
        

    }

}
