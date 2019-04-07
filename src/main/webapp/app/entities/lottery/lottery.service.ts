import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILottery } from 'app/shared/model/lottery.model';

type EntityResponseType = HttpResponse<ILottery>;
type EntityArrayResponseType = HttpResponse<ILottery[]>;

@Injectable({ providedIn: 'root' })
export class LotteryService {
    private resourceUrl = SERVER_API_URL + 'api/lotteries';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/lotteries';

    constructor(private http: HttpClient) {}

    create(lottery: ILottery): Observable<EntityResponseType> {
        return this.http.post<ILottery>(this.resourceUrl, lottery, { observe: 'response' });
    }

    update(lottery: ILottery): Observable<EntityResponseType> {
        return this.http.put<ILottery>(this.resourceUrl, lottery, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ILottery>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ILottery[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ILottery[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
