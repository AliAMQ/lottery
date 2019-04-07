import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LotterySharedModule } from 'app/shared';
import {
    PrizeComponent,
    PrizeDetailComponent,
    PrizeUpdateComponent,
    PrizeDeletePopupComponent,
    PrizeDeleteDialogComponent,
    prizeRoute,
    prizePopupRoute
} from './';

const ENTITY_STATES = [...prizeRoute, ...prizePopupRoute];

@NgModule({
    imports: [LotterySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PrizeComponent, PrizeDetailComponent, PrizeUpdateComponent, PrizeDeleteDialogComponent, PrizeDeletePopupComponent],
    entryComponents: [PrizeComponent, PrizeUpdateComponent, PrizeDeleteDialogComponent, PrizeDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LotteryPrizeModule {}
