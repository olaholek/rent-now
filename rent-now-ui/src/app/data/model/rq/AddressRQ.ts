export interface AddressRQ {
    city: string;
    street: string;
    houseNumber: string;
    apartmentNumber?: string | null;
    postalCode: string;
    post: string;
    country: string;
    county: string;
    province: string;
}
