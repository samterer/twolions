package br.com.maboo.fuellist.modelobj;

import android.content.Context;
import android.widget.ListView;

public class ListViewReport extends ListView {

	public ListViewReport(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private Double valorTotal = 0.0;
	private Double totalUnidade = 0.0;

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setTotalUnidade(Double totalUnidade) {
		this.totalUnidade = totalUnidade;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public Double getTotalUnidade() {
		return totalUnidade;
	}

}
