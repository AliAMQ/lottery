import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILottery } from 'app/shared/model/lottery.model';

@Component({
    selector: 'jhi-lottery-detail',
    templateUrl: './lottery-detail.component.html'
})
export class LotteryDetailComponent implements OnInit {
    lottery: ILottery;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ lottery }) => {
            this.lottery = lottery;
        });
    }

    previousState() {
        window.history.back();
    }
}
