export interface AddressRS {
    city: string;
    street: string | null;
    houseNumber: string;
    apartmentNumber?: string | null;
    postalCode: string;
    post: string;
    country: string;
    county: string;
    province: string;
}
