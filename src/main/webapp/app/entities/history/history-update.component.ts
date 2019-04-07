import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IHistory } from 'app/shared/model/history.model';
import { HistoryService } from './history.service';
import { ILottery } from 'app/shared/model/lottery.model';
import { LotteryService } from 'app/entities/lottery';

@Component({
    selector: 'jhi-history-update',
    templateUrl: './history-update.component.html'
})
export class HistoryUpdateComponent implements OnInit {
    private _history: IHistory;
    isSaving: boolean;

    lotteries: ILottery[];
    dateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private historyService: HistoryService,
        private lotteryService: LotteryService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ history }) => {
            this.history = history;
        });
        this.lotteryService.query().subscribe(
            (res: HttpResponse<ILottery[]>) => {
                this.lotteries = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.history.id !== undefined) {
            this.subscribeToSaveResponse(this.historyService.update(this.history));
        } else {
            this.subscribeToSaveResponse(this.historyService.create(this.history));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IHistory>>) {
        result.subscribe((res: HttpResponse<IHistory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackLotteryById(index: number, item: ILottery) {
        return item.id;
    }
    get history() {
        return this._history;
    }

    set history(history: IHistory) {
        this._history = history;
    }
}
