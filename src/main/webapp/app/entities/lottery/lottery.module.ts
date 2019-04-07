import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LotterySharedModule } from 'app/shared';
import {
    LotteryComponent,
    LotteryDetailComponent,
    LotteryUpdateComponent,
    LotteryDeletePopupComponent,
    LotteryDeleteDialogComponent,
    lotteryRoute,
    lotteryPopupRoute
} from './';

const ENTITY_STATES = [...lotteryRoute, ...lotteryPopupRoute];

@NgModule({
    imports: [LotterySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        LotteryComponent,
        LotteryDetailComponent,
        LotteryUpdateComponent,
        LotteryDeleteDialogComponent,
        LotteryDeletePopupComponent
    ],
    entryComponents: [LotteryComponent, LotteryUpdateComponent, LotteryDeleteDialogComponent, LotteryDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LotteryLotteryModule {}
