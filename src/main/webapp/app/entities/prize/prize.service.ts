import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPrize } from 'app/shared/model/prize.model';

type EntityResponseType = HttpResponse<IPrize>;
type EntityArrayResponseType = HttpResponse<IPrize[]>;

@Injectable({ providedIn: 'root' })
export class PrizeService {
    private resourceUrl = SERVER_API_URL + 'api/prizes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/prizes';

    constructor(private http: HttpClient) {}

    create(prize: IPrize): Observable<EntityResponseType> {
        return this.http.post<IPrize>(this.resourceUrl, prize, { observe: 'response' });
    }

    update(prize: IPrize): Observable<EntityResponseType> {
        return this.http.put<IPrize>(this.resourceUrl, prize, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPrize>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPrize[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPrize[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
