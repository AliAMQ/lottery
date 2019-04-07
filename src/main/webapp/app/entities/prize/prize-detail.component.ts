import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrize } from 'app/shared/model/prize.model';

@Component({
    selector: 'jhi-prize-detail',
    templateUrl: './prize-detail.component.html'
})
export class PrizeDetailComponent implements OnInit {
    prize: IPrize;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ prize }) => {
            this.prize = prize;
        });
    }

    previousState() {
        window.history.back();
    }
}
