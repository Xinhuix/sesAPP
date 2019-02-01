package com.jfinalshop.model.client;

import java.util.List;

import com.jfinalshop.model.SCard;
import com.jfinalshop.model.SCardmonth;
import com.jfinalshop.plugin.PaymentPlugin;

public class ReceiveCoupon_STUC {
			private List<SCard> cards;
			private List<SCardmonth> ms;
			private List<PaymentPlugin> pp;
			public List<SCard> getCards() {
				return cards;
			}
			public void setCards(List<SCard> cards) {
				this.cards = cards;
			}
			public List<SCardmonth> getMs() {
				return ms;
			}
			public void setMs(List<SCardmonth> ms) {
				this.ms = ms;
			}
			public List<PaymentPlugin> getPp() {
				return pp;
			}
			public void setPp(List<PaymentPlugin> pp) {
				this.pp = pp;
			}
			
			
}
