
modulesDependencies = [
		common: ['common'],
		ab: ['common', 'ab'],
		ab_sync: ['common', 'ab', 'ab_sync'],
		admin: ['common', 'admin'],
		bti: ['common', 'ab', 'bti'],
		mule: ['common', 'ab', 'mule'],
		tc: ['common', 'ab', 'bti', 'tc'],
		orgs: ['common', 'orgs'],
		payments: ['common', 'ab', 'admin', 'orgs', 'payments'],
		rent: ['common', 'ab', 'orgs', 'admin', 'payments', 'rent'],
		eirc: ['common', 'ab', 'admin', 'bti', 'orgs', 'payments', 'eirc'],
		sz: ['common', 'ab', 'admin', 'bti', 'orgs', 'payments', 'eirc', 'sz']
]
