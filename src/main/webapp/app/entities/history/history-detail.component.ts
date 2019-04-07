import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHistory } from 'app/shared/model/history.model';

@Component({
    selector: 'jhi-history-detail',
    templateUrl: './history-detail.component.html'
})
export class HistoryDetailComponent implements OnInit {
    history: IHistory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ history }) => {
            this.history = history;
        });
    }

    previousState() {
        window.history.back();
    }
}
